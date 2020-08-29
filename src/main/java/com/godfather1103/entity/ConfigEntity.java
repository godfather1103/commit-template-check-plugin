package com.godfather1103.entity;

import com.godfather1103.util.StringUtils;
import com.intellij.ide.util.PropertiesComponent;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

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

    private String path;
    private String jiraServer;
    private String jiraUserName;
    private String jiraPassword;

    public void initParam(@NotNull PropertiesComponent prop) {
        this.path = StringUtils.showString(prop.getValue(PATH));
        this.jiraServer = StringUtils.showString(prop.getValue(JIRA_SERVER_ADDRESS));
        this.jiraUserName = StringUtils.showString(prop.getValue(JIRA_USERNAME));
        this.jiraPassword = StringUtils.showString(prop.getValue(JIRA_PASSWORD));
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
}
