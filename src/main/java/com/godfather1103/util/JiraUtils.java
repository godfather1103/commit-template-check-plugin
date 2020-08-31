package com.godfather1103.util;

import com.godfather1103.entity.JiraEntity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    private final static OkHttpClient CLIENT = new OkHttpClient.Builder()
            .callTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build();

    private final static JsonParser PARSER = new JsonParser();

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
    public static List<JiraEntity> getToDoList(@NotNull String server, @NotNull String userName, @NotNull String password) throws Exception {
        String url = server + "/rest/api/2/search?jql=assignee=currentUser()+AND+resolution=Unresolved";
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", generateAuth(userName, password))
                .build();
        String response = CLIENT.newCall(request).execute().body().string();
        JsonObject jsonObject = PARSER.parse(response).getAsJsonObject();
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

    private static String generateAuth(@NotNull String userName, @NotNull String password) throws UnsupportedEncodingException {
        String base = "Basic ";
        String info = Base64.getEncoder().encodeToString((userName + ":" + password).getBytes("UTF-8"));
        return base + info;
    }
}
