package com.godfather1103.util;

import com.godfather1103.entity.JiraEntity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import groovy.lang.Tuple2;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * <p>Title:        Godfather1103's Github</p>
 * <p>Copyright:    Copyright (c) 2020</p>
 * <p>Company:      https://github.com/godfather1103</p>
 *
 * @author 作者: Jack Chu E-mail: chuchuanbao@gmail.com
 * 创建时间：2020-08-29 21:53
 * @version 1.0
 * @since 1.0
 * Jira相关工具类
 */
public class JiraUtils {
    private final static MediaType JSON = MediaType.parse("application/json");

    private final static ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("i18n/describe");

    public static Optional<Tuple2<String, String>> getSession(@NotNull String server, @NotNull String userName, @NotNull String password)
            throws Exception {
        String url = server + "/rest/auth/1/session";
        RequestBody body = buildUserInfoBody(userName, password);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        String response = HttpUtils.execute(request);
        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        JsonObject session = jsonObject.get("session").getAsJsonObject();
        return Optional.ofNullable(new Tuple2<>(session.get("name").getAsString(), session.get("value").getAsString()));
    }

    /**
     * 获取待处理的任务列表<BR>
     *
     * @param server   服务器地址
     * @param userName 用户名
     * @param password 密码
     * @return 相关列表
     * @throws Exception 查询列表过程中的异常
     * @author 作者: Jack Chu E-mail: chuchuanbao@gmail.com
     * 创建时间：2020-08-29 21:56
     */
    public static List<JiraEntity> getToDoList(@NotNull String server, @NotNull String userName, @NotNull String password)
            throws Exception {
        return getToDoList(server, userName, password, null);
    }

    /**
     * 获取待处理的任务列表<BR>
     *
     * @param server   服务器地址
     * @param userName 用户名
     * @param password 密码
     * @param jql      相关检索参数
     * @return 相关列表
     * @throws Exception 查询列表过程中的异常
     * @author 作者: Jack Chu E-mail: chuchuanbao@gmail.com
     * 创建时间：2020-08-29 21:56
     */
    public static List<JiraEntity> getToDoList(@NotNull String server, @NotNull String userName, @NotNull String password, String jql)
            throws Exception {
        if (jql == null || jql.trim().length() == 0) {
            jql = "assignee=currentUser()+AND+resolution=Unresolved";
        }
        String url = server + "/rest/api/2/search?jql=" + jql;
        Tuple2<String, String> session = getSession(server, userName, password).orElseThrow(() -> new RuntimeException(RESOURCE_BUNDLE.getString("jira_login_error")));
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .addHeader("cookie", session.getFirst() + "=" + session.getSecond())
                .build();
        String response = HttpUtils.execute(request);
        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        JsonArray issues = jsonObject.get("issues").getAsJsonArray();
        List<JiraEntity> list = new ArrayList<>(issues.size());
        issues.forEach(item -> {
            JsonObject issue = item.getAsJsonObject();
            String key = issue.get("key").getAsString();
            JsonObject fields = issue.getAsJsonObject("fields");
            list.add(new JiraEntity(key, fields.get("summary").getAsString()));
        });
        return list;
    }

    /**
     * 检测Jira服务器<BR>
     *
     * @param server 服务器地址
     * @return 检测结果
     * @author 作者: Jack Chu E-mail: chuchuanbao@gmail.com
     * 创建时间：2020-09-03 13:13
     */
    public static boolean checkJiraServer(@NotNull String server) {
        return HttpUtils.checkNetwork(server + "/rest/api/2/search").getFirst();
    }

    private static RequestBody buildUserInfoBody(@NotNull String userName, @NotNull String password) {
        JsonObject user = new JsonObject();
        user.addProperty("username", userName);
        user.addProperty("password", password);
        return RequestBody.create(JSON, user.toString());
    }
}
