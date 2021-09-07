package com.podigua.visark.server.cluster.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author: podigua
 * @create: 2021-09-06 20:40
 **/
public class ClusterChangeEvent extends ApplicationEvent {

    public ClusterChangeEvent() {
        super(new Object());
    }
}
