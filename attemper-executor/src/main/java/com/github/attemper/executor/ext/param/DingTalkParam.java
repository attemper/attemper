package com.github.attemper.executor.ext.param;

public class DingTalkParam {

    protected String url;

    protected String secret;

    protected String subject;

    protected String content;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
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
        if (getUrl() != null) {
            sb.append("url(" + getUrl() + ") ");
        }
        if (getSecret() != null) {
            sb.append("secret(" + getSecret() + ") ");
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
