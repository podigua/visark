package com.podigua.visark.server.data.service;

import com.podigua.visark.server.data.entity.Message;

import java.util.List;

/**
 * @author: podigua
 * @create: 2021-09-13 10:51
 **/
public interface KafkaSizeReceiveService {
    /**
     * 查询数据
     *
     * @param cluster 集群ID
     * @param topic   topic
     * @param offset  offset
     * @param size    大小
     * @return
     */
    List<Message> start(String cluster, String topic, String offset, Long size);
}
