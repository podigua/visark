package com.podigua.visark.server.admin.service.impl;

import com.podigua.visark.core.utils.KafkaAdminUtils;
import com.podigua.visark.server.admin.service.AdminCacheService;
import com.podigua.visark.server.cluster.entity.Cluster;
import com.podigua.visark.server.cluster.service.ClusterService;
import com.podigua.visark.server.option.service.OptionService;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author: podigua
 * @create: 2021-09-03 14:44
 **/
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AdminCacheServiceImpl implements AdminCacheService {
    private final static Map<String, KafkaAdminClient> clients = new HashMap<>();
    private final OptionService optionService;
    private final ClusterService clusterService;

    @Override
    public KafkaAdminClient getById(String id) {
        KafkaAdminClient client = clients.get(id);
        if (client == null) {
            Cluster cluster = clusterService.getById(id);
            Assert.notNull(cluster, "查找配置失败");
            client = KafkaAdminUtils.create(cluster.getBootstrapServers(), optionService.get());
            clients.put(id, client);
        }
        return client;
    }

    @Override
    public void close(String id) {
        Optional.ofNullable(clients.get(id)).ifPresent(KafkaAdminUtils::close);

    }
}
