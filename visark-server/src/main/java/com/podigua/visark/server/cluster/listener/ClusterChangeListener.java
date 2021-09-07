package com.podigua.visark.server.cluster.listener;

import com.podigua.visark.server.cluster.entity.Cluster;
import com.podigua.visark.server.cluster.event.ClusterChangeEvent;
import com.podigua.visark.server.cluster.event.ClusterUpdateEvent;
import com.podigua.visark.server.cluster.service.ClusterService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: podigua
 * @create: 2021-09-06 20:44
 **/
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ClusterChangeListener implements ApplicationListener<ClusterChangeEvent> {
    private final ClusterService clusterService;

    private final ApplicationContext applicationContext;

    @Override
    public void onApplicationEvent(ClusterChangeEvent event) {
        List<Cluster> clusters = clusterService.query4List();
        applicationContext.publishEvent(new ClusterUpdateEvent(clusters));
    }
}
