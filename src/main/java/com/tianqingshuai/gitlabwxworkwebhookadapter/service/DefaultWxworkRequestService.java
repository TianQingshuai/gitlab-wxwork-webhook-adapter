package com.tianqingshuai.gitlabwxworkwebhookadapter.service;

import com.tianqingshuai.gitlabwxworkwebhookadapter.data.BotKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author tianqingshuai
 */
@Service
@Slf4j
public class DefaultWxworkRequestService implements RequestService {
    private final String WXWORK_API_URL_PREFIX = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=";

    private RestTemplate restTemplate;

    private BotKey botKey;

    @Autowired
    public DefaultWxworkRequestService(RestTemplate restTemplate, BotKey botKey) {
        this.restTemplate = restTemplate;
        this.botKey = botKey;
    }

    @Override
    public boolean sendTextMsg(String content, String mentionedList, String mentionedMobileList) {
        String url = WXWORK_API_URL_PREFIX + botKey.getKey();
        String body = "{\"msgtype\": \"text\", \"text\": {\"content\": \"" + content + "\"}, \"mentioned_list\": [\"" + mentionedList + "\"], \"mentioned_mobile_list\": [\"" + mentionedMobileList + "\"]}";
        String result = restTemplate.postForObject(url, body, String.class);
        return result != null;
    }

    @Override
    public boolean sendMarkdownMsg(String markdown) {
        String url = WXWORK_API_URL_PREFIX + botKey.getKey();
        String body = "{\"msgtype\": \"markdown\", \"markdown\": {\"content\": \"" + markdown + "\"}}";
        String result = restTemplate.postForObject(url, body, String.class);
        return result != null;
    }
}
