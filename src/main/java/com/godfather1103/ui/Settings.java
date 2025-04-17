package com.godfather1103.ui;

import com.godfather1103.entity.ConfigEntity;
import com.godfather1103.settings.AppSettings;
import com.godfather1103.util.AESUtils;
import com.godfather1103.util.JiraUtils;
import com.godfather1103.util.StringUtils;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import io.vavr.control.Try;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.godfather1103.util.StringUtils.isEmpty;
import static com.godfather1103.util.StringUtils.showString;

/**
 * <p>Title:        Godfather1103's Github</p>
 * <p>Copyright:    Copyright (c) 2018</p>
 * <p>Company:      https://github.com/godfather1103</p>
 *
 * @author 作者: godfa E-mail: chuchuanbao@gmail.com
 * 创建时间：2018/11/3 23:29
 * @version 1.0
 * @since 1.0
 */
public class Settings implements Configurable {

    private final Project project;

    private final ResourceBundle bundle;

    public Settings(Project project) {
        this.project = project;
        this.bundle = ResourceBundle.getBundle("i18n/describe");
    }

    private TextFieldWithBrowseButton ruleConfFilePath;
    private JPanel rootPanel;
    private JTextField jiraUsername;
    private JPasswordField jiraPassword;
    private JTextField jiraServer;
    private JComboBox scopeSelectedMode;
    private JTextField jqlContent;
    private JCheckBox updateToSystem;

    @Override
    public String getDisplayName() {
        return Optional.of(bundle.getString("display_name"))
                .filter(StringUtils::isNotEmpty)
                .orElse("Git Commit Configuration");
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    @Override
    public void disposeUIResources() {

    }

    @Nullable
    @Override
    public JComponent createComponent() {
        FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
        ruleConfFilePath.addBrowseFolderListener(new TextBrowseFolderListener(descriptor) {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                String current = ruleConfFilePath.getText();
                if (!current.isEmpty()) {
                    fc.setCurrentDirectory(new File(current));
                }
                fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                fc.showOpenDialog(rootPanel);
                File file = fc.getSelectedFile();
                String path = file == null ? "" : file.getAbsolutePath();
                ruleConfFilePath.setText(path);
            }
        });

        for (ConfigEntity.SelectedMode value : ConfigEntity.SelectedMode.values()) {
            scopeSelectedMode.addItem(value);
        }
        return rootPanel;
    }

    @Override
    public boolean isModified() {
        AppSettings.State state = Objects.requireNonNull(AppSettings.getInstance(project).getState());
        String storedPath = showString(state.getPath());
        String storedJiraServerAddress = showString(state.getJiraServer());
        String storedJiraUserName = showString(state.getJiraUserName());
        String storedJiraPassword = showString(state.getJiraPassword());
        String storedJiraJql = showString(state.getJiraJql());
        ConfigEntity.SelectedMode storedSelectedMode = ConfigEntity.SelectedMode.getByKey(state.getSelectedMode())
                .orElse(ConfigEntity.SelectedMode.JIRAKEY);
        String uiPath = showString(ruleConfFilePath.getText());
        String uiAddress = showString(jiraServer.getText());
        String uiUserName = showString(jiraUsername.getText());
        String uiPassword = Try.of(() -> AESUtils.encrypt(showString(jiraPassword.getPassword()))).getOrNull();
        String uiJql = showString(jqlContent.getText());
        ConfigEntity.SelectedMode uiSelectedMode = ConfigEntity.SelectedMode.JIRAKEY;
        if (scopeSelectedMode.getSelectedIndex() != -1) {
            uiSelectedMode = (ConfigEntity.SelectedMode) scopeSelectedMode.getSelectedItem();
        }
        if (updateToSystem.isSelected()) {
            return true;
        } else if (!storedPath.equals(uiPath)) {
            return true;
        } else if (!storedJiraServerAddress.equals(uiAddress)) {
            return true;
        } else if (!storedJiraUserName.equals(uiUserName)) {
            return true;
        } else if (!storedJiraJql.equals(uiJql)) {
            return true;
        } else if (uiSelectedMode != storedSelectedMode) {
            return true;
        } else {
            return !storedJiraPassword.equals(uiPassword);
        }
    }


    @Override
    public void apply() {
        String server = showString(jiraServer.getText());
        if (!isEmpty(server) && !JiraUtils.checkJiraServer(server)) {
            if (server.endsWith("/")) {
                server = server.substring(0, server.length() - 1);
                if (!JiraUtils.checkJiraServer(server)) {
                    throw new RuntimeException("Jira Server[" + server + "] is Error!");
                } else {
                    jiraServer.setText(server);
                }
            } else {
                throw new RuntimeException("Jira Server[" + server + "] is Error!");
            }
        }
        AppSettings.State state = Objects.requireNonNull(AppSettings.getInstance(project).getState());
        state.setPath(showString(ruleConfFilePath.getText()));
        state.setJiraServer(showString(jiraServer.getText()));
        state.setJiraUserName(showString(jiraUsername.getText()));
        state.setJiraPassword(Try.of(() -> AESUtils.encrypt(showString(jiraPassword.getPassword()))).getOrNull());
        state.setJiraJql(showString(jqlContent.getText()));
        if (scopeSelectedMode.getSelectedIndex() != -1) {
            state.setSelectedMode(
                    Optional.ofNullable((ConfigEntity.SelectedMode) scopeSelectedMode.getSelectedItem())
                            .orElse(ConfigEntity.SelectedMode.JIRAKEY)
                            .getKey()
            );
        }
        state.setUseSystemConfig(project.isDefault());
        state.setTemplateProject(project.isDefault());
        // 默认工程强制更新全局信息
        if (updateToSystem.isSelected() || project.isDefault()) {
            state.applyToSystem();
            updateToSystem.setSelected(false);
        }
    }

    @Override
    public void reset() {
        AppSettings.State state = Objects.requireNonNull(AppSettings.getInstance(project).getState());
        ruleConfFilePath.setText(state.getPath());
        jiraServer.setText(state.getJiraServer());
        jiraUsername.setText(state.getJiraUserName());
        jiraPassword.setText(Try.of(() -> showString(AESUtils.decrypt(state.getJiraPassword()))).getOrNull());
        jqlContent.setText(state.getJiraJql());
        scopeSelectedMode.setSelectedItem(
                ConfigEntity.SelectedMode.getByKey(state.getSelectedMode())
                        .orElse(ConfigEntity.SelectedMode.JIRAKEY)
        );
        updateToSystem.setSelected(false);
    }
}
