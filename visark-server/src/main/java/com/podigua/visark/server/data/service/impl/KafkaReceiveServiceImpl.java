package com.podigua.visark.server.data.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.podigua.visark.server.admin.service.AdminService;
import com.podigua.visark.server.cluster.service.ClusterService;
import com.podigua.visark.server.data.entity.Message;
import com.podigua.visark.server.data.service.KafkaReceive;
import com.podigua.visark.server.data.service.KafkaReceiveService;
import com.podigua.visark.server.option.service.OptionService;
import com.podigua.visark.server.topic.service.TopicOptionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * @author: podigua
 * @create: 2021-09-13 10:54
 **/
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class KafkaReceiveServiceImpl implements KafkaReceiveService {
    private final static Map<String, KafkaReceive> receives = new ConcurrentHashMap<>();
    private final OptionService optionService;
    private final ClusterService clusterService;
    private final TopicOptionService topicOptionService;
    private final AdminService adminService;

    @Override
    public void init(WebSocketSession session) {
        String uuid = (String) session.getAttributes().get("uuid");
        String query = session.getUri().getQuery();
        String cluster = getQuery(query, "cluster");
        String topic = getQuery(query, "topic");
        KafkaReceive receive = new KafkaReceive(optionService.get(), clusterService.getById(cluster), topicOptionService.get(cluster, topic), adminService);
        Consumer<Message> consumer = message -> {
            try {
                session.sendMessage(new TextMessage(JSONObject.toJSONString(message)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        receive.start(consumer);
        receives.put(uuid, receive);
    }

    private String getQuery(String query, String key) {
        String[] array = query.split("&");
        for (String fieldValue : array) {
            String[] keyValue = fieldValue.split("=");
            if (keyValue.length == 2) {
                if (key.equals(keyValue[0])) {
                    return keyValue[1];
                }
            }
        }
        return "";
    }

    @Override
    public void close(WebSocketSession session) {
        KafkaReceive receive = receives.get((String) session.getAttributes().get("uuid"));
        if (receive != null) {
            receive.stop();
        }
    }
}
