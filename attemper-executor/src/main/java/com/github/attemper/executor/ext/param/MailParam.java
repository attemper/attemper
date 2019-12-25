package com.github.attemper.executor.ext.param;

public class MailParam {

    protected String from;

    protected String to;

    protected String subject;

    protected String content;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (getFrom() != null) {
            sb.append("from(" + getFrom() + ") ");
        }
        if (getTo() != null) {
            sb.append("to(" + getTo() + ") ");
        }
        if (getSubject() != null) {
            sb.append("subject(" + getSubject() + ") ");
        }
        if (getContent() != null) {
            sb.append("content(" + getContent() + ") ");
        }
        return sb.toString();
    }
}
