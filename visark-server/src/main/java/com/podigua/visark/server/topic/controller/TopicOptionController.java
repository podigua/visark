package com.podigua.visark.server.topic.controller;

import com.podigua.visark.core.annotation.WebResult;
import com.podigua.visark.server.topic.entity.TopicOption;
import com.podigua.visark.server.topic.service.TopicOptionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: podigua
 * @create: 2021-09-12 21:13
 **/
@RestController
@RequestMapping("/topic/option")
@AllArgsConstructor(onConstructor = @__(@Autowired))
@WebResult
public class TopicOptionController {
    private final TopicOptionService topicOptionService;

    /**
     * 保存
     *
     * @param topicOption 配置
     */
    @PostMapping
    public void save(@RequestBody TopicOption topicOption) {
        topicOptionService.save(topicOption);
    }

    /**
     * 获取
     *
     * @param cluster 集群
     * @param topic   topic
     * @return {@link TopicOption}
     */
    @GetMapping("/{cluster}/{topic}")
    public TopicOption get(@PathVariable String cluster, @PathVariable String topic) {
        return topicOptionService.get(cluster, topic);
    }
}
