package com.podigua.visark.server.data.service.impl;

import com.podigua.visark.server.admin.service.AdminService;
import com.podigua.visark.server.cluster.entity.Cluster;
import com.podigua.visark.server.cluster.service.ClusterService;
import com.podigua.visark.server.data.entity.Message;
import com.podigua.visark.server.data.service.KafkaSizeReceiveService;
import com.podigua.visark.server.option.entity.Option;
import com.podigua.visark.server.option.service.OptionService;
import com.podigua.visark.server.topic.entity.TopicOption;
import com.podigua.visark.server.topic.service.TopicOptionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author: podigua
 * @create: 2021-09-15 18:49
 **/
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class KafkaSizeReceiveServiceImpl implements KafkaSizeReceiveService {
    private final OptionService optionService;
    private final ClusterService clusterService;
    private final TopicOptionService topicOptionService;
    private final AdminService adminService;


    @Override
    public List<Message> start(String clusterId, String topic, String offset, Long size) {
        Option option = optionService.get();
        Cluster cluster = clusterService.getById(clusterId);
        TopicOption topicOption = topicOptionService.get(clusterId, topic);
        KafkaSizeReceive receive = new KafkaSizeReceive(option, cluster, topicOption, adminService, offset, size);
        List<Message> list = receive.start();
        Collections.reverse(list);
        return list;
    }
}
