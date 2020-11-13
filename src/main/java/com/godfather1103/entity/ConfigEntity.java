package com.godfather1103.entity;

import com.godfather1103.util.StringUtils;
import com.intellij.ide.util.PropertiesComponent;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
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

        private final int key;
        private final String value;

        SelectedMode(int key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return Integer.toString(key);
        }

        public static Optional<SelectedMode> getByKey(String key) {
            if (StringUtils.isEmpty(key)) {
                return Optional.of(JIRAKEY);
            }
            return Arrays.stream(SelectedMode.values())
                    .filter(item -> item.key == Integer.valueOf(key))
                    .findFirst();
        }

        ResourceBundle bundle = ResourceBundle.getBundle("i18n/describe");

        @Override
        public String toString() {
            return bundle.getString(this.value);
        }
    }

    private ConfigEntity(@NotNull PropertiesComponent prop) {
        initParam(prop);
    }

    public static Optional<ConfigEntity> getEntity(PropertiesComponent prop) {
        if (prop == null) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(new ConfigEntity(prop));
        }
    }

    public static final String PATH = "RuleConfFilePath";
    public static final String JIRA_SERVER_ADDRESS = "JIRA_SERVER_ADDRESS";
    public static final String JIRA_USERNAME = "JIRA_USERNAME";
    public static final String JIRA_PASSWORD = "JIRA_PASSWORD";
    public static final String JIRA_JQL = "JIRA_JQL";
    public static final String SCOPE_SELECTED_ITEM_INPUT_VALUE = "SCOPE_SELECTED_ITEM_INPUT_VALUE";

    private String path;
    private String jiraServer;
    private String jiraUserName;
    private String jiraPassword;
    private String jiraJQL;
    private SelectedMode selectedMode;

    public void initParam(@NotNull PropertiesComponent prop) {
        this.path = StringUtils.showString(prop.getValue(PATH));
        this.jiraServer = StringUtils.showString(prop.getValue(JIRA_SERVER_ADDRESS));
        this.jiraUserName = StringUtils.showString(prop.getValue(JIRA_USERNAME));
        this.jiraPassword = StringUtils.showString(prop.getValue(JIRA_PASSWORD));
        this.jiraJQL = StringUtils.showString(prop.getValue(JIRA_JQL));
        this.selectedMode = SelectedMode.getByKey(prop.getValue(SCOPE_SELECTED_ITEM_INPUT_VALUE)).orElse(SelectedMode.JIRAKEY);
    }

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
