package com.godfather1103.ui;

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
import java.util.ResourceBundle;

/**
 * <p>Title:        Godfather1103's Github</p>
 * <p>Copyright:    Copyright (c) 2018</p>
 * <p>Company:      https://github.com/godfather1103</p>
 *
 * @author 作者: godfa E-mail: chuchuanbao@gmail.com
 * 创建时间：2018/11/3 23:29
 * @version 1.0
 * @since
 */
public class Settings implements Configurable {

    ResourceBundle bundle = ResourceBundle.getBundle("i18n/describe");

    public static final String PATH = "RuleConfFilePath";
    public static final String JIRA_SERVER_ADDRESS = "JIRA_SERVER_ADDRESS";
    public static final String JIRA_USERNAME = "JIRA_USERNAME";
    public static final String JIRA_PASSWORD = "JIRA_PASSWORD";

    private TextFieldWithBrowseButton ruleConfFilePath;
    private JPanel rootPanel;
    private JTextField jira_username;
    private JPasswordField jira_password;
    private JTextField jira_server;

    @Override
    public String getDisplayName() {
        //"Git Commit Template Check Configuration";
        return bundle.getString("display_name");
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
        return rootPanel;
    }

    @Override
    public boolean isModified() {
        PropertiesComponent prop = PropertiesComponent.getInstance();
        String storedPath = showString(prop.getValue(PATH));
        String storedJiraServerAddress = showString(prop.getValue(JIRA_SERVER_ADDRESS));
        String storedJiraUserName = showString(prop.getValue(JIRA_USERNAME));
        String storedJiraPassword = showString(prop.getValue(JIRA_PASSWORD));
        String uiPath = showString(ruleConfFilePath.getText());
        String uiAddress = showString(jira_server.getText());
        String uiUserName = showString(jira_username.getText());
        String uiPassword = showString(jira_password.getPassword());
        return !storedPath.equals(uiPath)
                || !storedJiraServerAddress.equals(uiAddress)
                || !storedJiraUserName.equals(uiUserName)
                || !storedJiraPassword.equals(uiPassword);
    }

    private String showString(String str) {
        if (str == null || str.trim().length() == 0) {
            return "";
        } else {
            return str.trim();
        }
    }

    private String showString(char[] str) {
        if (str == null || str.length == 0) {
            return "";
        } else {
            return new String(str);
        }
    }

    @Override
    public void apply() {
        PropertiesComponent prop = PropertiesComponent.getInstance();
        prop.setValue(PATH, showString(ruleConfFilePath.getText()));
        prop.setValue(JIRA_SERVER_ADDRESS, showString(jira_server.getText()));
        prop.setValue(JIRA_USERNAME, showString(jira_username.getText()));
        prop.setValue(JIRA_PASSWORD, showString(jira_password.getPassword()));
    }

    @Override
    public void reset() {
        PropertiesComponent prop = PropertiesComponent.getInstance();
        ruleConfFilePath.setText(prop.getValue(PATH));
        jira_server.setText(prop.getValue(JIRA_SERVER_ADDRESS));
        jira_username.setText(prop.getValue(JIRA_USERNAME));
        jira_password.setText(prop.getValue(JIRA_PASSWORD));
    }
}
