package com.godfather1103.util;

import com.godfather1103.entity.JiraEntity;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.fail;

public class JiraUtilsTest {

    @Test
    public void getToDoList() {
        try {
            List<JiraEntity> r = JiraUtils.getToDoList(System.getenv("JIRA_SERVER"), System.getenv("JIRA_USERNAME"), System.getenv("JIRA_PASSWORD"));
            System.out.println(r.size());
        } catch (Exception e) {
            e.printStackTrace();
            fail("getToDoList异常");
        }
    }
}