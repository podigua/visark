package com.podigua.visark.server.cluster.listener;

import com.podigua.visark.server.cluster.event.ClusterChangeEvent;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

/**
 * @author: podigua
 * @create: 2021-09-06 20:57
 **/
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ClusterInitListener implements ApplicationListener<ApplicationStartedEvent> {
    private final ApplicationContext applicationContext;
    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        applicationContext.publishEvent(new ClusterChangeEvent());
    }
}
