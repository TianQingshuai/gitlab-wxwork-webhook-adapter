package com.tianqingshuai.gitlabwxworkwebhookadapter.data;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

/**
 * @author tianqingshuai
 */
@RequestScope
@Component
public class BotKey {

    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
