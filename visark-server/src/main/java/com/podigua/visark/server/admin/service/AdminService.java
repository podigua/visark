package com.podigua.visark.server.admin.service;

import com.podigua.visark.server.admin.TreeNode;
import com.podigua.visark.server.admin.entity.NewTopicEntity;

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
}
