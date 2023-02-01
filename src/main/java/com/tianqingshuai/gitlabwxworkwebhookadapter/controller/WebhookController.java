package com.tianqingshuai.gitlabwxworkwebhookadapter.controller;

import com.tianqingshuai.gitlabwxworkwebhookadapter.data.BotKey;
import javax.servlet.http.HttpServletRequest;


import com.tianqingshuai.gitlabwxworkwebhookadapter.listener.WebhookListener;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.webhook.WebHookManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tianqingshuai
 * @create 2023-01-31 18:49
 */

@RestController
@Slf4j
public class WebhookController implements InitializingBean {

    @Autowired
    private BotKey botKey;
    @Autowired
    WebhookListener webhookListener;

    private WebHookManager webHookManager;

    public WebhookController() {
        this.webHookManager = new WebHookManager();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        webHookManager.addListener(webhookListener);
    }

    @RequestMapping("/")
    public String index() {
        return "please request /webhook/${key}";
    }

    @RequestMapping("/webhook/{key}")
    public String webhook(@PathVariable("key") String key, HttpServletRequest request) {
        if (key == null || key.isEmpty()) {
            return "error";
        }
        botKey.setKey(key);

        try {
            webHookManager.handleEvent(request);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "error";
        }


        return "success";
    }
}
