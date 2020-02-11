package com.github.attemper.core.ext.notice;

import com.github.attemper.common.result.sys.tenant.Tenant;
import lombok.ToString;

import java.util.Map;

@ToString
public class MessageBean {

    protected String from;

    protected Tenant to;

    protected String subject;

    protected String content;

    /**
     * param to support more alarm types
     */
    protected Map<String, Object> extraMap;

    public String getFrom() {
        return from;
    }

    public MessageBean setFrom(String from) {
        this.from = from;
        return this;
    }

    public Tenant getTo() {
        return to;
    }

    public MessageBean setTo(Tenant to) {
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

    public Map<String, Object> getExtraMap() {
        return extraMap;
    }

    public MessageBean setExtraMap(Map<String, Object> extraMap) {
        this.extraMap = extraMap;
        return this;
    }
}
