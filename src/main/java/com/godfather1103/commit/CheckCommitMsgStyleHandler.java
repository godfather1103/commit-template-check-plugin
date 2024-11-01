package com.godfather1103.commit;


import com.godfather1103.settings.AppSettings;
import com.godfather1103.util.NotificationCenter;
import com.godfather1103.util.RuleCheckApp;
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
import java.util.Objects;
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

    private final ResourceBundle bundle;

    private final Project myProject;

    private final CheckinProjectPanel myCheckinPanel;

    private static boolean checkFlag = true;

    CheckCommitMsgStyleHandler(Project myProject, CheckinProjectPanel myCheckinPanel) {
        this.myProject = myProject;
        this.myCheckinPanel = myCheckinPanel;
        this.bundle = ResourceBundle.getBundle("i18n/describe");
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
        AppSettings.State state = Objects.requireNonNull(AppSettings.getInstance(myProject).getState());
        String path = state.getPath();
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
            } catch (Exception ex) {
                NotificationCenter.noticeWindows(bundle.getString("detection_result"), ex.getMessage(), NotificationCenter.TYPE_ERROR);
                return ReturnResult.CANCEL;
            }
        }
    }
}
