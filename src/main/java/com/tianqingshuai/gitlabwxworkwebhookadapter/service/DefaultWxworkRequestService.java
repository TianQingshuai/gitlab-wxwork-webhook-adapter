package com.tianqingshuai.gitlabwxworkwebhookadapter.service;

import com.tianqingshuai.gitlabwxworkwebhookadapter.data.BotKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

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
        HashMap<String, Object> stringObjectHashMap = new HashMap<>(16);
        stringObjectHashMap.put("msgtype", "text");
        HashMap<String, String> text = new HashMap<>(16);
        text.put("content", content);
        stringObjectHashMap.put("text", text);
        stringObjectHashMap.put("mentioned_list", mentionedList);
        stringObjectHashMap.put("mentioned_mobile_list", mentionedMobileList);
        log.info("wechat request:{}", stringObjectHashMap);
        String result = restTemplate.postForObject(url, stringObjectHashMap, String.class);
        log.info("wechat reponse:{}", result);
        return result != null;
    }

    @Override
    public boolean sendMarkdownMsg(String markdown) {
        String url = WXWORK_API_URL_PREFIX + botKey.getKey();
        HashMap<String, Object> stringObjectHashMap = new HashMap<>(16);
        stringObjectHashMap.put("msgtype", "markdown");
        HashMap<String, String> text = new HashMap<>(16);
        text.put("content", markdown);
        stringObjectHashMap.put("markdown", text);
        log.info("wechat request:{}", stringObjectHashMap);
        String result = restTemplate.postForObject(url, stringObjectHashMap, String.class);
        log.info("wechat reponse:{}", result);
        return result != null;
    }
}
