package com.podigua.visark.server.topic.service;

import com.podigua.visark.server.topic.entity.TopicOption;

/**
 * @author: podigua
 * @create: 2021-09-12 21:05
 **/
public interface TopicOptionService {
    /**
     * 保存
     * @param option
     */
    void save(TopicOption option);

    /**
     * 根据集群和topic获取<br/>
     * 若未保存,则获取全局Option
     * @param cluster 集群
     * @param topic topic
     * @return {@link TopicOption}
     */
    TopicOption get(String cluster,String topic);
}
