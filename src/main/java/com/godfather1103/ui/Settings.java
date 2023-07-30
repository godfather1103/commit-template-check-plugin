package com.godfather1103.ui;

import com.godfather1103.entity.ConfigEntity;
import com.godfather1103.util.JiraUtils;
import com.godfather1103.util.StringUtils;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
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

    ResourceBundle bundle = ResourceBundle.getBundle("i18n/describe");

    private TextFieldWithBrowseButton ruleConfFilePath;
    private JPanel rootPanel;
    private JTextField jiraUsername;
    private JPasswordField jiraPassword;
    private JTextField jiraServer;
    private JComboBox scopeSelectedMode;
    private JTextField jqlContent;

    @Override
    public String getDisplayName() {
        return Optional.ofNullable(bundle.getString("display_name"))
                .filter(StringUtils::isNotEmpty)
                .orElse("Git Commit Template Check Configuration");
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
        PropertiesComponent prop = PropertiesComponent.getInstance();
        String storedPath = showString(prop.getValue(ConfigEntity.PATH));
        String storedJiraServerAddress = showString(prop.getValue(ConfigEntity.JIRA_SERVER_ADDRESS));
        String storedJiraUserName = showString(prop.getValue(ConfigEntity.JIRA_USERNAME));
        String storedJiraPassword = showString(prop.getValue(ConfigEntity.JIRA_PASSWORD));
        String storedJiraJQL = showString(prop.getValue(ConfigEntity.JIRA_JQL));
        ConfigEntity.SelectedMode storedSelectedMode = ConfigEntity.SelectedMode
                .getByKey(showString(prop.getValue(ConfigEntity.SCOPE_SELECTED_ITEM_INPUT_VALUE)))
                .orElse(ConfigEntity.SelectedMode.JIRAKEY);
        String uiPath = showString(ruleConfFilePath.getText());
        String uiAddress = showString(jiraServer.getText());
        String uiUserName = showString(jiraUsername.getText());
        String uiPassword = showString(jiraPassword.getPassword());
        String uiJQL = showString(jqlContent.getText());
        ConfigEntity.SelectedMode uiSelectedMode = ConfigEntity.SelectedMode.JIRAKEY;
        if (scopeSelectedMode.getSelectedIndex() != -1) {
            uiSelectedMode = (ConfigEntity.SelectedMode) scopeSelectedMode.getSelectedItem();
        }
        return !storedPath.equals(uiPath)
                || !storedJiraServerAddress.equals(uiAddress)
                || !storedJiraUserName.equals(uiUserName)
                || !storedJiraJQL.equals(uiJQL)
                || uiSelectedMode != storedSelectedMode
                || !storedJiraPassword.equals(uiPassword);
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

        PropertiesComponent prop = PropertiesComponent.getInstance();
        prop.setValue(ConfigEntity.PATH, showString(ruleConfFilePath.getText()));
        prop.setValue(ConfigEntity.JIRA_SERVER_ADDRESS, showString(jiraServer.getText()));
        prop.setValue(ConfigEntity.JIRA_USERNAME, showString(jiraUsername.getText()));
        prop.setValue(ConfigEntity.JIRA_PASSWORD, showString(jiraPassword.getPassword()));
        prop.setValue(ConfigEntity.JIRA_PASSWORD, showString(jiraPassword.getPassword()));
        prop.setValue(ConfigEntity.JIRA_JQL, showString(jqlContent.getText()));
        if (scopeSelectedMode.getSelectedIndex() != -1) {
            prop.setValue(ConfigEntity.SCOPE_SELECTED_ITEM_INPUT_VALUE,
                    ((ConfigEntity.SelectedMode) scopeSelectedMode.getSelectedItem()).getKey());
        }
    }

    @Override
    public void reset() {
        PropertiesComponent prop = PropertiesComponent.getInstance();
        ruleConfFilePath.setText(prop.getValue(ConfigEntity.PATH));
        jiraServer.setText(prop.getValue(ConfigEntity.JIRA_SERVER_ADDRESS));
        jiraUsername.setText(prop.getValue(ConfigEntity.JIRA_USERNAME));
        jiraPassword.setText(prop.getValue(ConfigEntity.JIRA_PASSWORD));
        jqlContent.setText(prop.getValue(ConfigEntity.JIRA_JQL));
        scopeSelectedMode.setSelectedItem(ConfigEntity.SelectedMode
                .getByKey(showString(prop.getValue(ConfigEntity.SCOPE_SELECTED_ITEM_INPUT_VALUE)))
                .orElse(ConfigEntity.SelectedMode.JIRAKEY));
    }
}
