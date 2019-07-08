package com.github.attemper.core.ext.notice;

public class MessageBean {

    private String from;

    private String to;

    private String subject;

    private String content;

    public String getFrom() {
        return from;
    }

    public MessageBean setFrom(String from) {
        this.from = from;
        return this;
    }

    public String getTo() {
        return to;
    }

    public MessageBean setTo(String to) {
        this.to = to;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public MessageBean setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getContent() {
        return content;
    }

    public MessageBean setContent(String content) {
        this.content = content;
        return this;
    }
}
