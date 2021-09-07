package com.podigua.visark.server.cluster.event;

import com.podigua.visark.server.cluster.entity.Cluster;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * @author: podigua
 * @create: 2021-09-06 20:56
 **/
public class ClusterUpdateEvent extends ApplicationEvent {
    public ClusterUpdateEvent(List<Cluster> list) {
        super(list);
    }

    public List<Cluster> getCluster(){
        return (List<Cluster>) getSource();
    }
}
