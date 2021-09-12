package com.podigua.visark.server.admin.service;

import com.podigua.visark.server.admin.TreeNode;
import com.podigua.visark.server.admin.entity.NewTopicEntity;
import com.podigua.visark.server.admin.entity.NodeInfo;
import com.podigua.visark.server.admin.entity.TopicInfo;

import java.util.List;

/**
 * @author: podigua
 * @create: 2021-09-01 20:55
 **/
public interface AdminService {
    /**
     * 获取左侧树
     *
     * @param id
     * @return
     */
    List<TreeNode> getTrees(String id);

    /**
     * 获取Topics
     *
     * @param id
     * @return
     */
    List<TreeNode> getTopics(String id);

    /**
     * 创建topic
     *
     * @param id
     * @param topic
     */
    void createTopic(String id, NewTopicEntity topic);

    /**
     * 删除topic
     * @param id
     * @param topic
     */
    void deleteTopic(String id, String topic);

    /**
     * 查看节点信息
     * @param cluster
     * @param id
     * @return
     */
    NodeInfo getNode(String cluster, String id);

    /**
     * 获取所有节点信息
     * @param cluster
     * @return
     */
    List<NodeInfo> getNodes(String cluster);

    /**
     * 获取Topic 信息
     * @param cluster
     * @param topic
     * @return
     */
    TopicInfo getTopic(String cluster, String topic);
}
