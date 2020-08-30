package com.leroymerlin.commit;

import com.godfather1103.entity.ConfigEntity;
import com.godfather1103.entity.JiraEntity;
import com.godfather1103.util.JiraUtils;
import com.godfather1103.util.NotificationCenter;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.ui.DialogWrapper;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.util.List;
import java.util.Optional;

/**
 * @author Damien Arrachequesne
 */
public class CommitPanel {
    private JPanel mainPanel;
    private JComboBox changeType;
    private JComboBox changeScope;
    private JTextField shortDescription;
    private JTextArea longDescription;
    private JTextField closedIssues;
    private JTextField breakingChanges;

    private Optional<ConfigEntity> configEntity;

    CommitPanel(DialogWrapper dialog) {
        for (ChangeType type : ChangeType.values()) {
            changeType.addItem(type);
        }
        configEntity = ConfigEntity.getEntity(PropertiesComponent.getInstance());
        if (configEntity.isPresent() && configEntity.get().isOpenJira()) {
            ConfigEntity config = configEntity.get();
            try {
                List<JiraEntity> toDoList = JiraUtils.getToDoList(config.getJiraServer(), config.getJiraUserName(), config.getJiraPassword());
                if (toDoList != null) {
                    toDoList.forEach(changeScope::addItem);
                }
                changeScope.setSelectedIndex(-1);
                if (config.getSelectedMode() == ConfigEntity.SelectedMode.JIRAKEY
                        || config.getSelectedMode() == ConfigEntity.SelectedMode.JIRASUMMARY) {
                    changeScope.addItemListener(e -> {
                        if (e.getStateChange() == ItemEvent.SELECTED) {
                            Object item = e.getItem();
                            if (item instanceof JiraEntity) {
                                JiraEntity entity = (JiraEntity) item;
                                if (config.getSelectedMode() == ConfigEntity.SelectedMode.JIRAKEY) {
                                    changeScope.setSelectedItem(entity.getKey());
                                } else if (config.getSelectedMode() == ConfigEntity.SelectedMode.JIRASUMMARY) {
                                    changeScope.setSelectedItem(entity.getSummary());
                                }
                            }
                        }
                    });
                }
            } catch (Exception exception) {
                NotificationCenter.notice(exception.getMessage(), NotificationType.ERROR);
            }
        }
    }

    JPanel getMainPanel() {
        return mainPanel;
    }

    String getChangeType() {
        ChangeType type = (ChangeType) changeType.getSelectedItem();
        return type.label();
    }

    String getChangeScope() {
        if (changeScope.getSelectedIndex() == -1) {
            return changeScope.getSelectedItem().toString();
        } else {
            if (configEntity.isPresent() && configEntity.get().isOpenJira()) {
                ConfigEntity config = configEntity.get();
                if (config.getSelectedMode() == ConfigEntity.SelectedMode.SEE) {
                    return changeScope.getSelectedItem().toString();
                } else if (config.getSelectedMode() == ConfigEntity.SelectedMode.JIRASUMMARY) {
                    return ((JiraEntity) changeScope.getSelectedItem()).getSummary();
                }
                return ((JiraEntity) changeScope.getSelectedItem()).getKey();
            } else {
                return changeScope.getSelectedItem().toString();
            }
        }
    }

    String getShortDescription() {
        return shortDescription.getText().trim();
    }

    String getLongDescription() {
        return longDescription.getText().trim();
    }

    String getBreakingChanges() {
        return breakingChanges.getText().trim();
    }

    String getClosedIssues() {
        return closedIssues.getText().trim();
    }
}
