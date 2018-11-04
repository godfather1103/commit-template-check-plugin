package com.godfather1103.commit;


import com.godfather1103.app.RuleCheckApp;
import com.godfather1103.error.FailureException;
import com.godfather1103.ui.Settings;
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


/** 
 * <p>Title:        Godfather1103's Github</p>
 * <p>Copyright:    Copyright (c) 2018</p>
 * <p>Company:      https://github.com/godfather1103</p>
 * @author  作者: godfa E-mail: chuchuanbao@gmail.com
 * 创建时间：2018/11/3 23:36
 * @version 1.0  
 * @since  
 */
public class CheckCommitMsgStyleHandler extends CheckinHandler {

    private Project myProject;
    private CheckinProjectPanel myCheckinPanel;
    private static boolean checkFlag = false;

    public CheckCommitMsgStyleHandler(Project myProject,CheckinProjectPanel myCheckinPanel) {
        this.myProject = myProject;
        this.myCheckinPanel = myCheckinPanel;
    }

    @Nullable
    @Override
    public RefreshableOnComponent getBeforeCheckinConfigurationPanel() {
        NonFocusableCheckBox checkBox = new NonFocusableCheckBox("检查注释风格");
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
        PropertiesComponent prop = PropertiesComponent.getInstance();
        String path = prop.getValue(Settings.PATH);
        String sCommitMessage = myCheckinPanel.getCommitMessage();
        if (!checkFlag){
            return ReturnResult.COMMIT;
        }else {
            try {
                if (path!=null&&path.endsWith("json")){
                    new RuleCheckApp(path).check(sCommitMessage);
                }else{
                    new RuleCheckApp().check(sCommitMessage);
                }
                return ReturnResult.COMMIT;
            }catch (FailureException ex){
                NotificationCenter.noticeWindows("检测结果提醒",ex.getMessage(),NotificationCenter.TYPE_ERROR);
                return ReturnResult.CANCEL;
            }
        }
    }
}
