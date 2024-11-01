package com.leroymerlin.commit;

import com.godfather1103.entity.ConfigEntity;
import com.godfather1103.entity.JiraEntity;
import com.godfather1103.settings.AppSettings;
import com.godfather1103.util.JiraUtils;
import com.godfather1103.util.NotificationCenter;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    private List<Font> fonts;

    private synchronized void initFonts() {
        String[] data = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        int style = longDescription.getFont().getStyle();
        int size = longDescription.getFont().getSize();
        fonts = new ArrayList<>(data.length);
        for (String s : data) {
            fonts.add(new Font(s, style, size));
        }
        longDescription.addCaretListener(e -> {
            JTextArea ja = (JTextArea) e.getSource();
            String text = ja.getText();
            Font jaFont = ja.getFont();
            if (jaFont.canDisplayUpTo(text) != -1) {
                for (Font font : fonts) {
                    if (font.canDisplayUpTo(text) == -1) {
                        ja.setFont(font);
                        break;
                    }
                }
            }
        });
    }

    CommitPanel(Project project) {
        for (ChangeType type : ChangeType.values()) {
            changeType.addItem(type);
        }
        configEntity = ConfigEntity.getEntity(Objects.requireNonNull(AppSettings.getInstance(project).getState()));
        if (configEntity.isPresent() && configEntity.get().isOpenJira()) {
            ConfigEntity config = configEntity.get();
            try {
                List<JiraEntity> toDoList = null;
                try {
                    toDoList = JiraUtils.getToDoList(config.getJiraServer(), config.getJiraUserName(), config.getJiraPassword(), config.getJiraJQL());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    NotificationCenter.notice(ex.getMessage(), NotificationType.ERROR);
                }
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
                changeScope.setRenderer(
                        new ListCellRenderer() {

                            private ListCellRenderer renderer = changeScope.getRenderer();

                            @Override
                            public Component getListCellRendererComponent(JList list,
                                                                          Object value, int index, boolean isSelected, boolean cellHasFocus) {
                                Component component = renderer.getListCellRendererComponent(
                                        list, value, index, isSelected, cellHasFocus);
                                if (component instanceof JLabel) {
                                    JLabel label = (JLabel) component;
                                    label.setToolTipText(label.getText());
                                }
                                return component;
                            }
                        }
                );
            } catch (Exception exception) {
                exception.printStackTrace();
                NotificationCenter.notice(exception.getMessage(), NotificationType.ERROR);
            }
        }
        initFonts();
    }

    JPanel getMainPanel() {
        return mainPanel;
    }

    String getChangeType() {
        ChangeType type = (ChangeType) changeType.getSelectedItem();
        return type.label();
    }

    String getChangeScope() {
        if (changeScope.getSelectedItem() == null) {
            return "";
        }
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
