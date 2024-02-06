package com.godfather1103.vo;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>Title:        Godfather1103's Github</p>
 * <p>Copyright:    Copyright (c) 2024</p>
 * <p>Company:      https://github.com/godfather1103</p>
 * CheckRuleVo
 *
 * @author 作者: Jack Chu E-mail: chuchuanbao@gmail.com
 * @version 1.0
 * @date 创建时间：2024/2/6 13:56
 * @since 1.0
 */
public class CheckRuleVo implements Serializable {

    private String matchPattern;

    private String showMsg;

    public String getMatchPattern() {
        return matchPattern;
    }

    public void setMatchPattern(String matchPattern) {
        this.matchPattern = matchPattern;
    }

    public String getShowMsg() {
        return showMsg;
    }

    public void setShowMsg(String showMsg) {
        this.showMsg = showMsg;
    }

    public void check(String match) throws Exception {
        Pattern pattern = Pattern.compile(getMatchPattern());
        Matcher matcher = pattern.matcher(match);
        if (!matcher.find()) {
            throw new Exception(getShowMsg());
        }
    }
}
