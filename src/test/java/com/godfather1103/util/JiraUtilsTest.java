package com.godfather1103.util;

import com.godfather1103.entity.JiraEntity;
import io.vavr.Tuple2;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.fail;

public class JiraUtilsTest {

    @Test
    public void getToDoList() {
        try {
            List<JiraEntity> r = JiraUtils.getToDoList(System.getenv("JIRA_SERVER"), System.getenv("JIRA_USERNAME"), System.getenv("JIRA_PASSWORD"));
            System.out.println(r.size());
            r = JiraUtils.getToDoList(System.getenv("JIRA_SERVER"), System.getenv("JIRA_USERNAME"), System.getenv("JIRA_PASSWORD"),"project+=+XJRB+AND+resolution+=+Unresolved+AND+due+<=+1d+AND+assignee+in+(currentUser())");
            System.out.println(r.size());
        } catch (Exception e) {
            e.printStackTrace();
            fail("getToDoList异常");
        }
    }

    @Test
    public void getSession() {
        try {
            Optional<Tuple2<String, String>> session = JiraUtils.getSession(System.getenv("JIRA_SERVER"), System.getenv("JIRA_USERNAME"), System.getenv("JIRA_PASSWORD"));
            System.out.println(session.orElse(new Tuple2<>("", "")));
        } catch (Exception e) {
            e.printStackTrace();
            fail("getSession异常");
        }
    }
}