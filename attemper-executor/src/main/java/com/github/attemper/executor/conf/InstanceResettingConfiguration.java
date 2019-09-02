package com.github.attemper.executor.conf;

import com.github.attemper.common.enums.InstanceStatus;
import com.github.attemper.common.property.StatusProperty;
import com.github.attemper.common.result.dispatch.instance.Instance;
import com.github.attemper.config.base.conf.LocalServerConfig;
import com.github.attemper.core.service.instance.InstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class InstanceResettingConfiguration implements ApplicationListener<WebServerInitializedEvent> {

    @Autowired
    private LocalServerConfig localServerConfig;

    @Autowired
    private InstanceService instanceService;

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        String requestPath = localServerConfig.getLocalHost() + ":" + event.getWebServer().getPort();
        List<Instance> instances = instanceService.listRunningOfExecutor(requestPath);
        Date now = new Date();
        for (Instance instance : instances) {
            instance
                    .setStatus(InstanceStatus.FAILURE.getStatus())
                    .setEndTime(now)
                    .setDuration(now.getTime() - instance.getStartTime().getTime())
                    .setCode(3010)
                    .setMsg(StatusProperty.getValue(3010));
            instanceService.update(instance);
        }
    }
}
