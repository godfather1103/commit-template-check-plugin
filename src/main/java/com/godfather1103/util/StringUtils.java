package com.godfather1103.util;

/**
 * <p>Title:        Godfather1103's Github</p>
 * <p>Copyright:    Copyright (c) 2020</p>
 * <p>Company:      https://github.com/godfather1103</p>
 *
 * @author 作者: Jack Chu E-mail: chuchuanbao@gmail.com
 * 创建时间：2020-08-29 21:49
 * @version 1.0
 * @since 1.0
 * 字符串相关的工具类
 */
public class StringUtils {

    /**
     * 显示字符串，如果为空返回空串<BR>
     *
     * @param str 待处理的字符串
     * @return 处理后的结果
     * @author 作者: Jack Chu E-mail: chuchuanbao@gmail.com
     * 创建时间：2020-08-29 21:49
     */
    public static String showString(String str) {
        return showString(str, "");
    }

    public static String showString(String str, String defaultValue) {
        if (isEmpty(str)) {
            return defaultValue;
        } else {
            return str.trim();
        }
    }

    /**
     * 显示字符串，如果为空返回空串<BR>
     *
     * @param str 待处理的字符数组
     * @return 处理后的结果
     * @author 作者: Jack Chu E-mail: chuchuanbao@gmail.com
     * 创建时间：2020-08-29 21:49
     */
    public static String showString(char[] str) {
        if (str == null || str.length == 0) {
            return "";
        } else {
            return new String(str);
        }
    }

    /**
     * 判断字符串是否为空<BR>
     *
     * @param str 待处理的字符串
     * @return 判断结果
     * @author 作者: Jack Chu E-mail: chuchuanbao@gmail.com
     * 创建时间：2020-08-29 21:50
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
}
