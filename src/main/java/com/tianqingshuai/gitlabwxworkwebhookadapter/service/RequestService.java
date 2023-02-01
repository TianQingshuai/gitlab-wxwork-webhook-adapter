package com.tianqingshuai.gitlabwxworkwebhookadapter.service;

/**
 * @author tianqingshuai
 */
public interface RequestService {

    boolean sendTextMsg(String content, String mentionedList, String mentionedMobileList);

    boolean sendMarkdownMsg(String markdown);

    default boolean sendTextMsg(String content) {
        return sendTextMsg(content, null, null);
    }

    default boolean sendTextMsg(String content, String mentionedList) {
        return sendTextMsg(content, mentionedList, null);
    }
}
