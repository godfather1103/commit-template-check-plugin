package com.godfather1103.util;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.ui.Messages;

/**
 * <p>Title:        Godfather1103's Github</p>
 * <p>Copyright:    Copyright (c) 2018</p>
 * <p>Company:      https://github.com/godfather1103</p>
 *
 * @author 作者: godfa E-mail: chuchuanbao@gmail.com
 * 创建时间：2018/11/3 23:36
 * @version 1.0
 * @since
 */
public class NotificationCenter {

    public static int TYPE_INFO = 0;

    public static int TYPE_WARNING = 1;

    public static int TYPE_ERROR = 2;


    public static void notice(String message) {
        notice(message, NotificationType.INFORMATION);
    }

    public static void notice(String message, NotificationType type) {
        Notification n = new Notification(
                "extras",
                "Notice",
                message,
                type);
        Notifications.Bus.notify(n);
    }

    public static void noticeWindows(String title, String message) {
        noticeWindows(title, message, TYPE_INFO);
    }

    public static void noticeWindows(String title, String message, int nType) {
        if (nType == TYPE_ERROR) {
            Messages.showErrorDialog(message, title);
        } else if (nType == TYPE_WARNING) {
            Messages.showWarningDialog(message, title);
        } else {
            Messages.showInfoMessage(message, title);
        }
    }
}
