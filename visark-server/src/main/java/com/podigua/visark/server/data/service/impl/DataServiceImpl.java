package com.podigua.visark.server.data.service.impl;

import com.podigua.visark.server.cluster.entity.Cluster;
import com.podigua.visark.server.cluster.service.ClusterService;
import com.podigua.visark.server.data.entity.Message;
import com.podigua.visark.server.data.service.DataService;
import com.podigua.visark.server.data.service.KafkaReceive;
import com.podigua.visark.server.option.entity.Option;
import com.podigua.visark.server.option.service.OptionService;
import com.podigua.visark.server.topic.entity.TopicOption;
import com.podigua.visark.server.topic.service.TopicOptionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * @author: podigua
 * @create: 2021-09-12 23:10
 **/
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DataServiceImpl implements DataService {
    private final OptionService optionService;
    private final ClusterService clusterService;
    private final TopicOptionService topicOptionService;
    @Override
    @Async
    public void receive(String clusterId, String topic, SseEmitter emitter) {
        Cluster cluster = clusterService.getById(clusterId);
        Option option = optionService.get();
        TopicOption topicOption = topicOptionService.get(clusterId, topic);
        KafkaReceive receive=new KafkaReceive(option,cluster,topicOption);
        Consumer<Message> consumer=message->{
            try {
                emitter.send(SseEmitter.event().name("message").data(message));
                System.out.println("发送数据:"+message.getMessage());
            } catch (IOException e) {
            }
        };
        receive.start(consumer);
        emitter.onCompletion(()->{
            receive.stop();
        });
    }
}
