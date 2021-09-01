package com.github.attemper.core.ext.notice;

import com.github.attemper.config.base.bean.SpringContextAware;
import com.github.attemper.core.ext.notice.channel.Sender;
import org.reflections.Reflections;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class NoticeService implements ApplicationListener<WebServerInitializedEvent> {

    private final Map<Integer, Sender> senderMap = new HashMap<>();

    @Override
    public void onApplicationEvent(WebServerInitializedEvent webServerInitializedEvent) {
        Reflections reflections = new Reflections(Sender.class.getPackage().getName());
        Set<Class<? extends Sender>> subTypeSet = reflections.getSubTypesOf(Sender.class);
        for (Class<? extends Sender> subType : subTypeSet) {
            Sender sender = SpringContextAware.getBean(subType);
            if (senderMap.containsKey(sender.getIndex())) {
                throw new DuplicateKeyException("index is duplicated:" + sender.getIndex());
            }
            senderMap.put(sender.getIndex(), sender);
        }
    }

    public Map<Integer, Sender> getSenderMap() {
        return senderMap;
    }
}
