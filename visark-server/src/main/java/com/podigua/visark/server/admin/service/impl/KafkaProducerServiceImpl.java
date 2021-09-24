package com.podigua.visark.server.admin.service.impl;

import com.podigua.visark.core.expression.ExpressionExecutor;
import com.podigua.visark.server.admin.entity.KafkaMessage;
import com.podigua.visark.server.admin.service.KafkaProducerService;
import com.podigua.visark.server.cluster.entity.Cluster;
import com.podigua.visark.server.cluster.service.ClusterService;
import com.podigua.visark.server.data.entity.Message;
import com.podigua.visark.server.option.entity.Option;
import com.podigua.visark.server.option.service.OptionService;
import com.podigua.visark.server.programme.entity.Programme;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author: podigua
 * @create: 2021-09-24 16:59
 **/
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class KafkaProducerServiceImpl implements KafkaProducerService {
    private final ClusterService clusterService;
    private final OptionService optionService;
    private final ExpressionExecutor expressionExecutor;

    @Override
    public void send(String id, String topic, KafkaMessage message) {
        Cluster cluster = clusterService.getById(id);
        Option option = optionService.get();
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, cluster.getBootstrapServers());
        properties.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, option.getTimeout() + "");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        KafkaProducer producer = new KafkaProducer<>(properties);
        String key = parseMessage(message.getKey(), message.getExpressions());
        String value = parseMessage(message.getValue(), message.getExpressions());
        ProducerRecord record = new ProducerRecord<>(topic, key, value);
        producer.send(record);
    }

    private String parseMessage(String content, List<Programme.Expression> expressions) {
        if (StringUtils.isEmpty(content)) {
            return null;
        }
        Map<String, Object> values = new HashMap<>();
        if(!CollectionUtils.isEmpty(expressions)){
            expressions.forEach(expression -> {
                String code = expression.getCode();
                Object value = expressionExecutor.getValue(expression.getExpression(), null, values);
                values.put(code, value);
            });
        }
        return expressionExecutor.getValueByTemplate(content, null, values);
    }
}
