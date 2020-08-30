package com.leroymerlin.commit;

import com.godfather1103.entity.ConfigEntity;
import com.godfather1103.entity.JiraEntity;
import com.godfather1103.util.JiraUtils;
import com.godfather1103.util.NotificationCenter;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.ui.DialogWrapper;

import javax.swing.*;
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

    private Optional<ConfigEntity> jira;

    CommitPanel(DialogWrapper dialog) {
        for (ChangeType type : ChangeType.values()) {
            changeType.addItem(type);
        }
        jira = ConfigEntity.getEntity(PropertiesComponent.getInstance());
        // TODO 待实现
        if (jira.isPresent() && jira.get().isOpenJira()) {
            try {
                List<JiraEntity> toDoList = JiraUtils.getToDoList(jira.get().getJiraServer(), jira.get().getJiraUserName(), jira.get().getJiraPassword());
                if (toDoList != null) {
                    toDoList.forEach(changeScope::addItem);
                }
                changeScope.setSelectedIndex(-1);
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
            return ((JiraEntity) changeScope.getSelectedItem()).getKey();
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
