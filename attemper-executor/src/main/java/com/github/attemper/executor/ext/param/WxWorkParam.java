package com.github.attemper.executor.ext.param;

public class WxWorkParam {

    protected String url;

    protected String content;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
        if (getContent() != null) {
            sb.append("content(" + getContent() + ") ");
        }
        return sb.toString();
    }
}
