package com.godfather1103.settings;

import com.godfather1103.util.StringUtils;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Optional;

import static com.godfather1103.util.StringUtils.showString;

/**
 * <p>Title:        Godfather1103's Github</p>
 * <p>Copyright:    Copyright (c) 2024</p>
 * <p>Company:      <a href="https://github.com/godfather1103">https://github.com/godfather1103</a></p>
 * 类描述：
 *
 * @author 作者: Jack Chu E-mail: chuchuanbao@gmail.com
 * @version 1.0
 * @date 创建时间：2024/11/1 16:59
 * @since 1.0
 */
@Service(Service.Level.PROJECT)
@State(
        name = "com.godfather1103.settings.AppSettings",
        storages = @Storage("ComGodfather1103SettingsAppSettings.xml")
)
public final class AppSettings implements PersistentStateComponent<AppSettings.State> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppSettings.class);

    public static final String PATH = "RuleConfFilePath";
    public static final String JIRA_SERVER_ADDRESS = "JIRA_SERVER_ADDRESS";
    public static final String JIRA_USERNAME = "JIRA_USERNAME";
    public static final String JIRA_PASSWORD = "JIRA_PASSWORD";
    public static final String JIRA_JQL = "JIRA_JQL";
    public static final String SCOPE_SELECTED_ITEM_INPUT_VALUE = "SCOPE_SELECTED_ITEM_INPUT_VALUE";
    public static final String SYSTEM_FLAG = "system";
    public static final String USER_FLAG = "user";

    private State myState = new State();

    private static State systemState;

    public static AppSettings getInstance(Project project) {
        loadSystemState();
        var app = project.getService(AppSettings.class);
        if (Objects.nonNull(app.getState())) {
            app.getState().setTemplateProject(project.isDefault());
        }
        return app;
    }

    @Override
    public State getState() {
        if (Objects.isNull(myState.getUseSystemConfig())
                || Optional.ofNullable(myState.getTemplateProject()).orElse(false)) {
            loadState(myState);
        }
        return myState;
    }

    @Override
    public void loadState(@NotNull State state) {
        loadSystemState();
        // 默认工程统一使用系统变量
        if (Optional.ofNullable(myState.getTemplateProject()).orElse(false)) {
            state.setUseSystemConfig(true);
        }
        // 非默认工程根据实际情况注入
        if (Objects.isNull(state.getUseSystemConfig())) {
            state.setUseSystemConfig(StringUtils.isEmpty(state.getJiraServer()));
        }
        // 第一次加载时注入其中
        if (state.getUseSystemConfig()) {
            systemState.copyTo(state);
            state.setUseSystemConfig(Optional.ofNullable(state.getTemplateProject()).orElse(false));
        }
        this.myState = state;
    }

    /**
     * loadSystemState<BR>
     *
     * @author 作者: Jack Chu E-mail: chuchuanbao@gmail.com
     * @date 创建时间：2025/4/17 15:11
     */
    private static void loadSystemState() {
        if (Objects.isNull(systemState)) {
            synchronized (State.class) {
                if (Objects.isNull(systemState)) {
                    State tmp = new State(SYSTEM_FLAG);
                    PropertiesComponent prop = PropertiesComponent.getInstance();
                    String storedPath = showString(prop.getValue(PATH));
                    String storedJiraServerAddress = showString(prop.getValue(JIRA_SERVER_ADDRESS));
                    String storedJiraUserName = showString(prop.getValue(JIRA_USERNAME));
                    String storedJiraPassword = showString(prop.getValue(JIRA_PASSWORD));
                    String storedJiraJql = showString(prop.getValue(JIRA_JQL));
                    tmp.setPath(storedPath);
                    tmp.setJiraServer(storedJiraServerAddress);
                    tmp.setJiraUserName(storedJiraUserName);
                    tmp.setJiraPassword(storedJiraPassword);
                    tmp.setJiraJql(storedJiraJql);
                    tmp.setSelectedMode(prop.getInt(SCOPE_SELECTED_ITEM_INPUT_VALUE, 1));
                    systemState = tmp;
                }
            }
        }
    }

    public static class State {

        private final String type;

        public State() {
            this(USER_FLAG);
        }

        private State(String type) {
            this.type = type;
        }

        private Boolean useSystemConfig;
        private Boolean templateProject;
        private String path;
        private String jiraServer;
        private String jiraUserName;
        private String jiraPassword;
        private String jiraJql;
        private Integer selectedMode;

        /**
         * applyToSystem<BR>
         *
         * @author 作者: Jack Chu E-mail: chuchuanbao@gmail.com
         * @date 创建时间：2025/4/17 15:08
         */
        public void applyToSystem() {
            if (Objects.equals(SYSTEM_FLAG, getType())) {
                LOGGER.warn("系统State不能调度该方法");
                return;
            }
            copyTo(systemState);
            PropertiesComponent prop = PropertiesComponent.getInstance();
            prop.setValue(PATH, showString(systemState.getPath()));
            prop.setValue(JIRA_SERVER_ADDRESS, showString(systemState.getJiraServer()));
            prop.setValue(JIRA_USERNAME, showString(systemState.getJiraUserName()));
            prop.setValue(JIRA_PASSWORD, showString(systemState.getJiraPassword()));
            prop.setValue(JIRA_JQL, showString(systemState.getJiraJql()));
            prop.setValue(SCOPE_SELECTED_ITEM_INPUT_VALUE, Optional.ofNullable(systemState.getSelectedMode()).orElse(1), 1);
        }

        /**
         * copyTo<BR>
         *
         * @param to 参数
         * @author 作者: Jack Chu E-mail: chuchuanbao@gmail.com
         * @date 创建时间：2025/4/17 15:05
         */
        private void copyTo(@NotNull State to) {
            to.setPath(getPath());
            to.setJiraServer(getJiraServer());
            to.setJiraUserName(getJiraUserName());
            to.setJiraPassword(getJiraPassword());
            to.setJiraJql(getJiraJql());
            to.setSelectedMode(getSelectedMode());
        }

        public String getType() {
            return type;
        }

        public Boolean getTemplateProject() {
            return templateProject;
        }

        public void setTemplateProject(Boolean templateProject) {
            this.templateProject = templateProject;
        }

        public Boolean getUseSystemConfig() {
            return useSystemConfig;
        }

        public void setUseSystemConfig(Boolean useSystemConfig) {
            this.useSystemConfig = useSystemConfig;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getJiraServer() {
            return jiraServer;
        }

        public void setJiraServer(String jiraServer) {
            this.jiraServer = jiraServer;
        }

        public String getJiraUserName() {
            return jiraUserName;
        }

        public void setJiraUserName(String jiraUserName) {
            this.jiraUserName = jiraUserName;
        }

        public String getJiraPassword() {
            return jiraPassword;
        }

        public void setJiraPassword(String jiraPassword) {
            this.jiraPassword = jiraPassword;
        }

        public String getJiraJql() {
            return jiraJql;
        }

        public void setJiraJql(String jiraJql) {
            this.jiraJql = jiraJql;
        }

        public Integer getSelectedMode() {
            return selectedMode;
        }

        public void setSelectedMode(Integer selectedMode) {
            this.selectedMode = selectedMode;
        }
    }
}
