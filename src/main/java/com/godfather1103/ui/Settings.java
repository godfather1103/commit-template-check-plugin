package com.godfather1103.ui;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;

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

    public static final String PATH = "RuleConfFilePath";

    private TextFieldWithBrowseButton ruleConfFilePath;
    private JPanel rootPanel;

    @Override
    public String getDisplayName() {
        return "Commit Temp Check Configuration";
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
        String storedPath = prop.getValue(PATH);
        String uiPath = ruleConfFilePath.getText();
        if (storedPath == null) {
            storedPath = "";
        }
        return !storedPath.equals(uiPath);
    }

    @Override
    public void apply() throws ConfigurationException {
        PropertiesComponent prop = PropertiesComponent.getInstance();
        prop.setValue(PATH, ruleConfFilePath.getText());
    }

    @Override
    public void reset() {
        PropertiesComponent prop = PropertiesComponent.getInstance();
        ruleConfFilePath.setText(prop.getValue(PATH));
    }
}
