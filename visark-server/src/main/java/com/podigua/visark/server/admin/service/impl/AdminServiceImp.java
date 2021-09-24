package com.podigua.visark.server.admin.service.impl;

import com.podigua.visark.core.utils.KafkaAdminUtils;
import com.podigua.visark.server.admin.NodeType;
import com.podigua.visark.server.admin.TreeNode;
import com.podigua.visark.server.admin.entity.NewTopicEntity;
import com.podigua.visark.server.admin.entity.NodeInfo;
import com.podigua.visark.server.admin.entity.TopicInfo;
import com.podigua.visark.server.admin.service.AdminCacheService;
import com.podigua.visark.server.admin.service.AdminService;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author: podigua
 * @create: 2021-09-01 20:58
 **/
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AdminServiceImp implements AdminService {
    private final AdminCacheService adminCacheService;

    @Override
    public List<TreeNode> getTrees(String id) {
        List<TreeNode> result = new ArrayList<>();
        KafkaAdminClient client = adminCacheService.getById(id);
        addClusters(result, client);
        addTopics(result, client);
        addConsumers(result, client);
        return result;
    }

    @Override
    public List<TreeNode> getTopics(String id) {
        KafkaAdminClient client = adminCacheService.getById(id);
        return getTopics(client);
    }

    @Override
    public void createTopic(String id, NewTopicEntity topic) {
        KafkaAdminClient client = adminCacheService.getById(id);
        List<NewTopic> topics = new ArrayList<>();
        NewTopic newTopic = new NewTopic(topic.getName(), topic.getPartitions(), topic.getReplication());
        newTopic.configs(topic.getConfigs());
        topics.add(newTopic);
        CreateTopicsResult result = client.createTopics(topics);
        result.values().forEach((key, value) -> {
            try {
                value.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException("创建失败");
            }
        });
    }

    @Override
    public void deleteTopic(String id, String topic) {
        KafkaAdminClient client = adminCacheService.getById(id);
        List<String> topics = new ArrayList<>();
        topics.add(topic);
        DeleteTopicsResult result = client.deleteTopics(topics);
        result.values().forEach((key, value) -> {
            try {
                value.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException("删除失败");
            }
        });
    }

    @Override
    public NodeInfo getNode(String cluster, String id) {
        KafkaAdminClient client = adminCacheService.getById(cluster);
        Collection<Node> nodes = KafkaAdminUtils.nodes(client);
        return nodes.stream().filter(node -> node.idString().equals(id)).map(NodeInfo::of).findFirst().orElse(new NodeInfo());
    }

    @Override
    public List<NodeInfo> getNodes(String cluster) {
        KafkaAdminClient client = adminCacheService.getById(cluster);
        Collection<Node> nodes = KafkaAdminUtils.nodes(client);
        return nodes.stream().map(NodeInfo::of).collect(Collectors.toList());
    }

    @Override
    public TopicInfo getTopic(String cluster, String topic) {
        KafkaAdminClient client = adminCacheService.getById(cluster);
        TopicDescription description = KafkaAdminUtils.topic(client, topic);
        return TopicInfo.of(description);
    }

    @Override
    public void deleteConsumer(String id, String groupId) {
        KafkaAdminClient client = adminCacheService.getById(id);
        DeleteConsumerGroupsResult result = client.deleteConsumerGroups(Collections.singletonList(groupId));
        result.deletedGroups().forEach((key,value)->{
            try {
                value.get();
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        });
    }

    @Override
    public void newPartitions(String id, String topic, Integer count) {
        KafkaAdminClient client = adminCacheService.getById(id);
        KafkaAdminUtils.newPartitions(client,topic,count);
    }

    private void addConsumers(List<TreeNode> result, KafkaAdminClient client) {
        TreeNode consumer = TreeNode.create(NodeType.CONSUMER, NodeType.CONSUMER.name(), "Consumers");
        List<String> consumers = KafkaAdminUtils.consumers(client);
        List<TreeNode> children = consumers.stream().map(groupId -> TreeNode.create(NodeType.CONSUMER_INSTANCE, groupId, groupId)).collect(Collectors.toList());
        consumer.setChildren(children);
        result.add(consumer);
    }

    private void addTopics(List<TreeNode> result, KafkaAdminClient client) {
        TreeNode topics = TreeNode.create(NodeType.TOPIC, NodeType.TOPIC.name(), "Topics");
        List<TreeNode> children = getTopics(client);
        topics.setChildren(children);
        result.add(topics);
    }

    private void addClusters(List<TreeNode> result, KafkaAdminClient client) {
        TreeNode cluster = TreeNode.create(NodeType.NODE, NodeType.NODE.name(), "Nodes");
        Collection<Node> nodes = KafkaAdminUtils.nodes(client);
        List<TreeNode> children = nodes.stream().map(node -> TreeNode.create(NodeType.NODE_INSTANCE, node.idString(), node.host() + ":" + node.port())).collect(Collectors.toList());
        cluster.setChildren(children);
        result.add(cluster);
    }

    /**
     * 获取所有Topic
     *
     * @param client
     * @return
     */
    private List<TreeNode> getTopics(KafkaAdminClient client) {
        List<String> names = KafkaAdminUtils.topics(client);
        return names.stream().map(name -> TreeNode.create(NodeType.TOPIC_INSTANCE, name, name)).collect(Collectors.toList());
    }
}
