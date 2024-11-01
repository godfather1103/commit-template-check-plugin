package com.godfather1103.entity;

import com.godfather1103.settings.AppSettings;
import com.godfather1103.util.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.ResourceBundle;

/**
 * <p>Title:        Godfather1103's Github</p>
 * <p>Copyright:    Copyright (c) 2020</p>
 * <p>Company:      https://github.com/godfather1103</p>
 *
 * @author 作者: Jack Chu E-mail: chuchuanbao@gmail.com
 * 创建时间：2020-08-29 23:18
 * @version 1.0
 * @since 1.0
 * 配置相关对象
 */
public class ConfigEntity {

    public enum SelectedMode {
        /**
         * 只填充jira Key
         */
        JIRAKEY(1, "jira_key_mode"),
        /**
         * 填充jira标题
         */
        JIRASUMMARY(2, "jira_summary_mode"),
        /**
         * 所见即所得
         */
        SEE(3, "see_mode");

        private final Integer key;
        private final String value;

        final ResourceBundle bundle;

        SelectedMode(Integer key, String value) {
            this.key = key;
            this.value = value;
            bundle = ResourceBundle.getBundle("i18n/describe");
        }

        public Integer getKey() {
            return key;
        }

        /**
         * getByKey<BR>
         *
         * @param key 参数
         * @return 结果
         * @author 作者: Jack Chu E-mail: chuchuanbao@gmail.com
         * @date 创建时间：2024/11/1 18:39
         */
        public static Optional<SelectedMode> getByKey(Integer key) {
            for (SelectedMode mode : SelectedMode.values()) {
                if (mode.key.equals(key)) {
                    return Optional.of(mode);
                }
            }
            return Optional.of(JIRAKEY);
        }

        @Override
        public String toString() {
            return bundle.getString(this.value);
        }
    }

    private ConfigEntity(@NotNull AppSettings.State state) {
        this.path = StringUtils.showString(state.getPath());
        this.jiraServer = StringUtils.showString(state.getJiraServer());
        this.jiraUserName = StringUtils.showString(state.getJiraUserName());
        this.jiraPassword = StringUtils.showString(state.makeDecryptJiraPassword());
        this.jiraJQL = StringUtils.showString(state.getJiraJql());
        this.selectedMode = SelectedMode.getByKey(state.getSelectedMode()).orElse(SelectedMode.JIRAKEY);
    }

    public static Optional<ConfigEntity> getEntity(AppSettings.State state) {
        if (state == null) {
            return Optional.empty();
        } else {
            return Optional.of(new ConfigEntity(state));
        }
    }

    private String path;
    private String jiraServer;
    private String jiraUserName;
    private String jiraPassword;
    private String jiraJQL;
    private SelectedMode selectedMode;

    public boolean isOpenJira() {
        if (StringUtils.isEmpty(jiraServer)) {
            return false;
        }
        if (StringUtils.isEmpty(jiraUserName)) {
            return false;
        }
        if (StringUtils.isEmpty(jiraPassword)) {
            return false;
        }
        return true;
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

    public SelectedMode getSelectedMode() {
        return selectedMode;
    }

    public void setSelectedMode(SelectedMode selectedMode) {
        this.selectedMode = selectedMode;
    }

    public String getJiraJQL() {
        return jiraJQL;
    }

    public void setJiraJQL(String jiraJQL) {
        this.jiraJQL = jiraJQL;
    }
}
