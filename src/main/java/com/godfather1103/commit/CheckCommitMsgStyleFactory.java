package com.godfather1103.commit;

import com.intellij.openapi.vcs.CheckinProjectPanel;
import com.intellij.openapi.vcs.changes.CommitContext;
import com.intellij.openapi.vcs.checkin.CheckinHandler;
import com.intellij.openapi.vcs.checkin.CheckinHandlerFactory;
import org.jetbrains.annotations.NotNull;

/** 
 * <p>Title:        Godfather1103's Github</p>
 * <p>Copyright:    Copyright (c) 2018</p>
 * <p>Company:      https://github.com/godfather1103</p>
 * @author  作者: godfa E-mail: chuchuanbao@gmail.com
 * 创建时间：2018/11/3 23:36
 * @version 1.0  
 * @since  
 */
public class CheckCommitMsgStyleFactory extends CheckinHandlerFactory {
    @NotNull
    @Override
    public CheckinHandler createHandler(@NotNull CheckinProjectPanel checkinProjectPanel, @NotNull CommitContext commitContext) {
        return new CheckCommitMsgStyleHandler(checkinProjectPanel.getProject(),checkinProjectPanel);
    }
}
