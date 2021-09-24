package com.podigua.visark.server.admin.controller;

import com.podigua.visark.core.annotation.WebResult;
import com.podigua.visark.core.utils.ClipboardUtils;
import com.podigua.visark.server.admin.TreeNode;
import com.podigua.visark.server.admin.entity.KafkaMessage;
import com.podigua.visark.server.admin.entity.NewTopicEntity;
import com.podigua.visark.server.admin.entity.NodeInfo;
import com.podigua.visark.server.admin.entity.TopicInfo;
import com.podigua.visark.server.admin.service.AdminService;
import com.podigua.visark.server.admin.service.KafkaProducerService;
import lombok.AllArgsConstructor;
import org.apache.kafka.common.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: podigua
 * @create: 2021-09-01 20:55
 **/
@RestController
@RequestMapping("/admin")
@WebResult
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AdminRestController {

    private final AdminService adminService;
    private final KafkaProducerService kafkaProducerService;

    /**
     * 获取左侧树
     *
     * @return
     */
    @GetMapping("/{id}/trees")
    public List<TreeNode> getTrees(@PathVariable String id) {
        return adminService.getTrees(id);
    }

    /**
     * 获取Topics
     *
     * @return
     */
    @GetMapping("/{id}/topics")
    public List<TreeNode> getTopics(@PathVariable String id) {
        return adminService.getTopics(id);
    }

    /**
     * 创建topic
     *
     * @param id
     * @param topic
     */
    @PostMapping("/{id}/topic/create")
    public void createTopic(@PathVariable String id, @RequestBody NewTopicEntity topic) {
        adminService.createTopic(id, topic);
    }

    /**
     * 新增 分片
     *
     * @param id
     * @param topic
     * @param count
     */
    @PutMapping("/{id}/{topic}/partitions/new")
    public void newPartitions(@PathVariable String id, @PathVariable String topic, Integer count) {
        adminService.newPartitions(id, topic, count);
    }


    /**
     * 删除topic
     *
     * @param id    集群
     * @param topic topic
     */
    @DeleteMapping("/topic/{id}/{topic}")
    public void deleteTopic(@PathVariable String id, @PathVariable String topic) {
        adminService.deleteTopic(id, topic);
    }

    /**
     * 删除consumer
     *
     * @param id    集群
     * @param consumer consumer
     */
    @DeleteMapping("/consumer/{id}/{consumer}")
    public void deleteConsumer(@PathVariable String id, @PathVariable String consumer) {
        adminService.deleteConsumer(id, consumer);
    }

    /**
     * 获取Node信息
     *
     * @param cluster 集群
     * @param id      nodeId
     * @return {@link Node}
     */
    @GetMapping("/{cluster}/node/{id}")
    public NodeInfo getNode(@PathVariable String cluster, @PathVariable String id) {
        return adminService.getNode(cluster, id);
    }

    /**
     * 获取所有节点信息
     *
     * @param cluster
     * @return
     */
    @GetMapping("/{cluster}/nodes")
    public List<NodeInfo> getNodes(@PathVariable String cluster) {
        return adminService.getNodes(cluster);
    }

    /**
     * 获取Topic信息
     *
     * @param cluster
     * @param topic
     * @return
     */
    @GetMapping("/{cluster}/topic/{topic}")
    public TopicInfo getTopic(@PathVariable String cluster, @PathVariable String topic) {
        return adminService.getTopic(cluster, topic);
    }

    /**
     * 复制
     *
     * @param text
     */
    @PostMapping("/copy")
    public void copy(@RequestBody String text) {
        ClipboardUtils.copy(text);
    }

    @PostMapping("/{id}/{topic}/send")
    public void send(@PathVariable String id, @PathVariable String topic,@RequestBody KafkaMessage message) {
        kafkaProducerService.send(id,topic,message);
    }
}
