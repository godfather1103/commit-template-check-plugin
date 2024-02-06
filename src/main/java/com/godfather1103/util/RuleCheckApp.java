package com.godfather1103.util;

import com.godfather1103.vo.CheckRuleVo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * <p>Title:        Godfather1103's Github</p>
 * <p>Copyright:    Copyright (c) 2024</p>
 * <p>Company:      https://github.com/godfather1103</p>
 * RuleCheckApp
 *
 * @author 作者: Jack Chu E-mail: chuchuanbao@gmail.com
 * @version 1.0
 * @date 创建时间：2024/2/6 14:15
 * @since 1.0
 */
public class RuleCheckApp {

    private static final String BASE_RULE = "[\n" +
            "  {\n" +
            "    \"matchPattern\": \"(feat|fix|docs|style|refactor|perf|test|build|ci|chore|revert)(\\\\(\\\\S*\\\\))?:\\\\s.+\",\n" +
            "    \"showMsg\": \"未包含feat,fix,docs,style,refactor,perf,test,build,ci,chore,revert等关键词\"\n" +
            "  }\n" +
            "]";

    private ArrayList<CheckRuleVo> ruleCheckers;

    public ArrayList<CheckRuleVo> getRuleCheckers() {
        return ruleCheckers;
    }

    public void setRuleCheckers(ArrayList<CheckRuleVo> ruleCheckers) {
        this.ruleCheckers = ruleCheckers;
    }

    public RuleCheckApp() {
        init(BASE_RULE);
    }

    public RuleCheckApp(String configFilePath) {
        try {
            String s = FileUtils.readFileToString(new File(configFilePath), "UTF-8");
            init(s);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * init<BR>
     *
     * @param content 参数
     * @author 作者: Jack Chu E-mail: chuchuanbao@gmail.com
     * @date 创建时间：2024/2/6 14:15
     */
    private void init(String content) {
        String configContent = StringUtils.showString(content, BASE_RULE);
        Type type = new TypeToken<ArrayList<CheckRuleVo>>() {
        }.getType();
        setRuleCheckers(new Gson().fromJson(configContent, type));
    }


    /**
     * 检测方法<BR>
     *
     * @param text 被检测的结果
     * @throws Exception 相关检测的异常
     * @author 作者: godfa E-mail: chuchuanbao@gmail.com
     * 创建时间：2018/11/3 22:57
     */
    public void check(String text) throws Exception {
        if (this.ruleCheckers == null) {
            throw new Exception("相关规则配置文件不存在！");
        } else {
            for (CheckRuleVo ruleChecker : this.ruleCheckers) {
                ruleChecker.check(text);
            }
        }
    }
}
