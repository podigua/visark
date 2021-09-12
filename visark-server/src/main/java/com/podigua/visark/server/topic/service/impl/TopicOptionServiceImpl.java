package com.podigua.visark.server.topic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.podigua.visark.server.option.entity.Option;
import com.podigua.visark.server.option.service.OptionService;
import com.podigua.visark.server.topic.dao.TopicOptionDao;
import com.podigua.visark.server.topic.entity.TopicOption;
import com.podigua.visark.server.topic.service.TopicOptionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author: podigua
 * @create: 2021-09-12 21:06
 **/
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class TopicOptionServiceImpl implements TopicOptionService {
    private final TopicOptionDao topicOptionDao;
    private final OptionService optionService;
    @Override
    public void save(TopicOption topicOption) {
        if(StringUtils.isEmpty(topicOption.getId())){
            topicOptionDao.insert(topicOption);
        }else{
            topicOptionDao.updateById(topicOption);
        }
    }

    @Override
    public TopicOption get(String cluster, String topic) {
        LambdaQueryWrapper<TopicOption> wrapper = Wrappers.lambdaQuery(TopicOption.class).eq(TopicOption::getClusterId, cluster)
                .eq(TopicOption::getTopic, topic);
        TopicOption topicOption = topicOptionDao.selectOne(wrapper);
        if(topicOption==null){
            Option option = optionService.get();
            return TopicOption.create(cluster,topic,option.getKeyDeserializer(),option.getValueDeserializer());
        }
        return topicOption;
    }
}
