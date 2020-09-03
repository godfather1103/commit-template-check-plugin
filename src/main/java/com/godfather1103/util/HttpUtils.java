package com.godfather1103.util;

import com.intellij.notification.NotificationType;
import groovy.lang.Tuple2;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

/**
 * <p>Title:        Godfather1103's Github</p>
 * <p>Copyright:    Copyright (c) 2020</p>
 * <p>Company:      https://github.com/godfather1103</p>
 *
 * @author 作者: Jack Chu E-mail: chuchuanbao@gmail.com
 * 创建时间：2020-09-03 13:14
 * @version 1.0
 * @since 1.0
 * HTTP工具
 */
public class HttpUtils {

    private final static OkHttpClient CLIENT = new OkHttpClient.Builder()
            .callTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build();

    private final static ResourceBundle bundle = ResourceBundle.getBundle("i18n/describe");

    public static String execute(Request request) throws IOException {
        Response response = exec(request);
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            String msg = String.format(bundle.getString("network_error") + " Url[%s],Code[%s]", request.url(), response.code());
            NotificationCenter.notice(msg, NotificationType.ERROR);
            throw new RuntimeException(msg);
        }
    }

    public static Response exec(Request request) throws IOException {
        return CLIENT.newCall(request).execute();
    }

    public static Tuple2<Boolean, Integer> checkNetwork(@NotNull String url) {
        if (StringUtils.isEmpty(url)) {
            throw new RuntimeException("URL is Empty!");
        }
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = exec(request);
            return new Tuple2<>(response.isSuccessful(), response.code());
        } catch (IOException e) {
            NotificationCenter.notice(e.getMessage(), NotificationType.ERROR);
            throw new RuntimeException(bundle.getString("network_error"));
        }
    }
}
