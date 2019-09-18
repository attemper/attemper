package com.github.attemper.config.base.conf;

import com.github.attemper.common.property.StatusProperty;
import com.github.attemper.config.base.property.AppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(-1)
@Component
public class LocaleConfig implements ApplicationListener<WebServerInitializedEvent> {

    @Autowired
    private AppProperties appProperties;

    @Override
    public void onApplicationEvent(WebServerInitializedEvent webServerInitializedEvent) {
        StatusProperty.initResourceBundle(appProperties.getLocaleInfo().getLanguage(), appProperties.getLocaleInfo().getCountry());
    }
}
