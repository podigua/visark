package com.podigua.visark.server.admin.service;

import org.apache.kafka.clients.admin.KafkaAdminClient;

/**
 * @author: podigua
 * @create: 2021-09-03 14:43
 **/
public interface AdminCacheService {
    /**
     * 根据ID获取
     *
     * @param id
     * @return
     */
    KafkaAdminClient getById(String id);

    /**
     * 关闭
     *
     * @param id
     */
    void close(String id);
}
