package com.godfather1103.commit;


import com.godfather1103.app.RuleCheckApp;
import com.godfather1103.entity.ConfigEntity;
import com.godfather1103.error.FailureException;
import com.godfather1103.util.NotificationCenter;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.CheckinProjectPanel;
import com.intellij.openapi.vcs.checkin.CheckinHandler;
import com.intellij.openapi.vcs.ui.RefreshableOnComponent;
import com.intellij.ui.NonFocusableCheckBox;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ResourceBundle;


/**
 * <p>Title:        Godfather1103's Github</p>
 * <p>Copyright:    Copyright (c) 2018</p>
 * <p>Company:      https://github.com/godfather1103</p>
 *
 * @author 作者: godfa E-mail: chuchuanbao@gmail.com
 * 创建时间：2018/11/3 23:36
 * @version 1.0
 * @since 1.0
 */
public class CheckCommitMsgStyleHandler extends CheckinHandler {

    ResourceBundle bundle = ResourceBundle.getBundle("i18n/describe");

    private Project myProject;
    private CheckinProjectPanel myCheckinPanel;
    private static boolean checkFlag = true;

    CheckCommitMsgStyleHandler(Project myProject, CheckinProjectPanel myCheckinPanel) {
        this.myProject = myProject;
        this.myCheckinPanel = myCheckinPanel;
    }

    @Nullable
    @Override
    public RefreshableOnComponent getBeforeCheckinConfigurationPanel() {
        NonFocusableCheckBox checkBox = new NonFocusableCheckBox(bundle.getString("check_label_message"));
        return new RefreshableOnComponent() {
            @Override
            public JComponent getComponent() {
                JPanel panel = new JPanel(new BorderLayout());
                panel.add(checkBox);
                boolean dumb = DumbService.isDumb(myProject);
                checkBox.setEnabled(!dumb);
                return panel;
            }

            @Override
            public void refresh() {

            }

            @Override
            public void saveState() {
                checkFlag = checkBox.isSelected();
            }

            @Override
            public void restoreState() {
                checkBox.setSelected(checkFlag);
            }
        };
    }


    @Override
    public ReturnResult beforeCheckin() {
        String basePath = myProject.getBasePath();
        String filePath = basePath + "/check.commit.style.rule.json";
        PropertiesComponent prop = PropertiesComponent.getInstance();
        String path = prop.getValue(ConfigEntity.PATH);
        String sCommitMessage = myCheckinPanel.getCommitMessage();
        if (!checkFlag) {
            return ReturnResult.COMMIT;
        } else {
            try {
                if (new File(filePath).exists()) {
                    new RuleCheckApp(filePath).check(sCommitMessage);
                } else if (path != null && new File(path).exists()) {
                    new RuleCheckApp(path).check(sCommitMessage);
                } else {
                    new RuleCheckApp().check(sCommitMessage);
                }
                return ReturnResult.COMMIT;
            } catch (FailureException ex) {
                NotificationCenter.noticeWindows(bundle.getString("detection_result"), ex.getMessage(), NotificationCenter.TYPE_ERROR);
                return ReturnResult.CANCEL;
            }
        }
    }
}
