package com.tianqingshuai.gitlabwxworkwebhookadapter.listener;

import com.tianqingshuai.gitlabwxworkwebhookadapter.service.RequestService;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.webhook.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author tianqingshuai
 */
@Slf4j
@Component
public class WebhookListener implements WebHookListener {
    @Autowired
    private RequestService requestService;

    @Override
    public void onPushEvent(PushEvent event) {
        List<EventCommit> commits = event.getCommits();
        Collections.reverse(commits);

        StringBuilder content = new StringBuilder();
        content.append(event.getUserName())
                .append(" 推送：\n")
                .append("代码库：\t")
                .append("[").append(event.getProject().getName()).append("]")
                .append("(").append(event.getProject().getWebUrl()).append(")")
                .append("\n 分支：\t")
                .append("[").append(event.getBranch()).append("]")
                .append("(").append(event.getProject().getWebUrl()).append("/tree/").append(event.getBranch()).append(")")
                .append("\n");

        for (EventCommit commit : commits) {
            String shortId = commit.getId().substring(0, 6);
            content.append("> ")
                    .append("[").append(shortId).append("]")
                    .append("(").append(commit.getUrl()).append(")")
                    .append(" : ")
                    .append(commit.getMessage())
                    .append("\n");
        }

        requestService.sendMarkdownMsg(content.toString());
    }

    @Override
    public void onMergeRequestEvent(MergeRequestEvent event) {
        log.debug(event.toString());

        StringBuilder content = new StringBuilder();
        String sourceBranch = event.getObjectAttributes().getSourceBranch();
        String sourceBranchUrl = event.getProject().getWebUrl() + "/tree/" + sourceBranch;
        String targetBranch = event.getObjectAttributes().getTargetBranch();
        String targetBranchUrl = event.getProject().getWebUrl() + "/tree/" + targetBranch;
        String state = event.getObjectAttributes().getState();

        String action = "open";
        if (event.getChanges().getTitle() != null) {
            action = "update";
        }
        if (Objects.equals(state, "merged")) {
            action = "merged";
        }
        if (Objects.equals(state, "closed")) {
            action = "close";
        }

        content.append(event.getUser().getName())
                .append(" MergeRequest ：\nAction：\t")
                .append(action)
                .append("\n 代码库:\t")
                .append("[").append(event.getProject().getName()).append("]")
                .append("(").append(event.getProject().getWebUrl()).append(")")
                .append("\n 源分支:\t")
                .append("[").append(sourceBranch).append("]")
                .append("(").append(sourceBranchUrl).append(")")
                .append("\n 目标分支:\t")
                .append("[").append(targetBranch).append("]")
                .append("(").append(targetBranchUrl).append(")")
                .append("\n")
                .append("> ")
                .append("[").append(event.getObjectAttributes().getTitle()).append("]")
                .append("(").append(event.getObjectAttributes().getUrl()).append(")")
                .append("\n")
                .append("状态 : ").append(state).append(" | ").append(event.getObjectAttributes().getMergeStatus()).append("\n");

        requestService.sendMarkdownMsg(content.toString());
    }

    @Override
    public void onTagPushEvent(TagPushEvent event) {
        log.debug(event.toString());

        StringBuilder content = new StringBuilder();
        String projectName = event.getProject().getName();
        String projectUrl = event.getProject().getWebUrl();
        String tagName = event.getRef().replaceAll("refs/tags/", "");
        String tagUrl = projectUrl + "/tree/" + tagName;

        content.append(event.getUserName())
                .append(" 推送Tag：\n")
                .append("代码库:\t")
                .append("[").append(projectName).append("]")
                .append("(").append(projectUrl).append(")")
                .append("\n分支：\t")
                .append("[").append(tagName).append("]")
                .append("(").append(tagUrl).append(")");

        requestService.sendMarkdownMsg(content.toString());
    }

    @Override
    public void onNoteEvent(NoteEvent noteEvent) {
        log.debug(noteEvent.toString());

        StringBuilder content = new StringBuilder();
        String projectName = noteEvent.getProject().getName();
        String projectUrl = noteEvent.getProject().getWebUrl();
        NoteEvent.NoteableType noteableType = noteEvent.getObjectAttributes().getNoteableType();

        switch(noteableType) {
            case ISSUE:
                String issueTitle = noteEvent.getIssue().getTitle();
                String issueUrl = noteEvent.getIssue().getUrl();
                String issueState = noteEvent.getIssue().getState();
                String noteAuthor = noteEvent.getUser().getName();
                String noteBody = noteEvent.getObjectAttributes().getNote();

                content.append(noteAuthor).append(" 评论issue：\n")
                        .append("issue:\t")
                        .append("[").append(issueTitle).append("]")
                        .append("(").append(issueUrl).append(")\n")
                        .append("代码库:\t")
                        .append("[").append(projectName).append("]")
                        .append("(").append(projectUrl).append(")")
                        .append("\n")
                        .append("> ")
                        .append(noteBody)
                        .append("\n")
                        .append("Status : ").append(issueState).append(" | ");

                requestService.sendMarkdownMsg(content.toString());
                break;
            case MERGE_REQUEST:
                String mergeRequestTitle = noteEvent.getMergeRequest().getTitle();
                String mergeRequestUrl = noteEvent.getMergeRequest().getUrl();
                String mergeRequestState = noteEvent.getMergeRequest().getState();
                String mergeRequestNoteAuthor = noteEvent.getUser().getName();
                String mergeRequestNoteBody = noteEvent.getObjectAttributes().getNote();

                content.append(mergeRequestNoteAuthor).append("评论MergeRequest：\n")
                        .append("URL：\t")
                        .append("[").append(mergeRequestTitle).append("]")
                        .append("(").append(mergeRequestUrl).append(")")
                        .append("\n代码库：\t")
                        .append("[").append(projectName).append("]")
                        .append("(").append(projectUrl).append(")")
                        .append("\n")
                        .append("评论内容:\n")
                        .append("> ")
                        .append(mergeRequestNoteBody)
                        .append("\n\n")
                        .append("Status : ").append(mergeRequestState);

                requestService.sendMarkdownMsg(content.toString());
                break;
            case COMMIT:
                String commitId = noteEvent.getCommit().getId();
                String commitUrl = noteEvent.getCommit().getUrl();
                String commitNoteAuthor = noteEvent.getUser().getName();
                String commitNoteBody = noteEvent.getObjectAttributes().getNote();

                content.append(commitNoteAuthor).append(" 评论Commit：\n")
                        .append("commit：\t")
                        .append("[").append(commitId).append("]")
                        .append("(").append(commitUrl).append(")")
                        .append("\n代码库：\t")
                        .append("[").append(projectName).append("]")
                        .append("(").append(projectUrl).append(")")
                        .append("\n")
                        .append("> ")
                        .append(commitNoteBody);

                requestService.sendMarkdownMsg(content.toString());
                break;
            case SNIPPET:
                String snippetTitle = noteEvent.getSnippet().getTitle();
                String snippetNoteAuthor = noteEvent.getUser().getName();
                String snippetNoteBody = noteEvent.getObjectAttributes().getNote();

                content.append(snippetNoteAuthor).append("评论：\n")
                        .append("snippet：\t ")
                        .append(" ").append(snippetTitle).append(" ")
                        .append("\n代码库:\t")
                        .append("[").append(projectName).append("]")
                        .append("(").append(projectUrl).append(")")
                        .append("\n")
                        .append("> ")
                        .append(snippetNoteBody);

                requestService.sendMarkdownMsg(content.toString());
                break;
            default:
                break;
        }

    }
}
