package com.godfather1103.settings;

import com.godfather1103.util.AESUtils;
import com.godfather1103.util.StringUtils;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import io.vavr.control.Try;
import org.jetbrains.annotations.NotNull;

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

    public static AppSettings getInstance(Project project) {
        return project.getService(AppSettings.class);
    }

    @Override
    public State getState() {
        return myState;
    }

    @Override
    public void loadState(@NotNull State state) {
        this.myState = state;
    }

    private State myState = new State();

    public static class State {

        private String path;
        private String jiraServer;
        private String jiraUserName;
        private String jiraPassword;
        private String jiraJql;
        private Integer selectedMode;

        private static final String ENC_KEY = "1qaz!@#$2wsx%^&*";

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

        public String makeDecryptJiraPassword() {
            return Try.of(() -> AESUtils.decrypt(getJiraPassword(), ENC_KEY)).getOrNull();
        }

        public void setJiraPassword(String jiraPassword) {
            if (StringUtils.isNotEmpty(jiraPassword)) {
                this.jiraPassword = Try.of(() -> AESUtils.encrypt(jiraPassword, ENC_KEY)).getOrNull();
            }
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
