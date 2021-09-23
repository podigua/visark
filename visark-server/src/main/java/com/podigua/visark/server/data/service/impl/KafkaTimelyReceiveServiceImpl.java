package com.podigua.visark.server.data.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.podigua.visark.server.admin.service.AdminService;
import com.podigua.visark.server.cluster.service.ClusterService;
import com.podigua.visark.server.data.entity.Message;
import com.podigua.visark.server.data.service.KafkaTimelyReceiveService;
import com.podigua.visark.server.data.utils.QueryUtils;
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
public class KafkaTimelyReceiveServiceImpl implements KafkaTimelyReceiveService {
    private final static Map<String, KafkaTimelyReceive> receives = new ConcurrentHashMap<>();
    private final OptionService optionService;
    private final ClusterService clusterService;
    private final TopicOptionService topicOptionService;
    private final AdminService adminService;

    @Override
    public void init(WebSocketSession session) {
        String uuid = (String) session.getAttributes().get("uuid");
        String query = session.getUri().getQuery();
        String cluster = QueryUtils.getString(query, "cluster");
        String topic = QueryUtils.getString(query, "topic");
        String offset = QueryUtils.getString(query, "offset");
        KafkaTimelyReceive receive = new KafkaTimelyReceive(optionService.get(), clusterService.getById(cluster), topicOptionService.get(cluster, topic), adminService, offset);
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


    @Override
    public void close(WebSocketSession session) {
        KafkaTimelyReceive receive = receives.get((String) session.getAttributes().get("uuid"));
        if (receive != null) {
            receive.stop();
        }
    }
}
