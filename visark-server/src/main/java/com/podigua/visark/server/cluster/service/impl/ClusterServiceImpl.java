package com.podigua.visark.server.cluster.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.podigua.visark.server.cluster.dao.ClusterDao;
import com.podigua.visark.server.cluster.entity.Cluster;
import com.podigua.visark.server.cluster.event.ClusterChangeEvent;
import com.podigua.visark.server.cluster.service.ClusterService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author: podigua
 * @create: 2021-09-01 17:41
 **/
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class ClusterServiceImpl implements ClusterService {
    private final ClusterDao clusterDao;
    private final ApplicationContext applicationContext;

    @Override
    public void save(Cluster cluster) {
        if (StringUtils.isEmpty(cluster.getId())) {
            clusterDao.insert(cluster);
        } else {
            clusterDao.updateById(cluster);
        }
        applicationContext.publishEvent(new ClusterChangeEvent());
    }

    @Override
    public void deleteById(String id) {
        log.info("删除集群:{}", id);
        clusterDao.deleteById(id);
        applicationContext.publishEvent(new ClusterChangeEvent());
    }

    @Override
    public Cluster getById(String id) {
        return clusterDao.selectById(id);
    }

    @Override
    public List<Cluster> query4List() {
        LambdaQueryWrapper<Cluster> wrapper = Wrappers.lambdaQuery(Cluster.class).orderByAsc(Cluster::getCreateTime);
        return clusterDao.selectList(wrapper);
    }
}
