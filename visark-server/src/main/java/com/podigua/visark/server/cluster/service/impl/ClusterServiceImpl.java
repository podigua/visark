package com.podigua.visark.server.cluster.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.podigua.visark.server.cluster.dao.ClusterDao;
import com.podigua.visark.server.cluster.entity.Cluster;
import com.podigua.visark.server.cluster.params.ClusterParams;
import com.podigua.visark.server.cluster.service.ClusterService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author: podigua
 * @create: 2021-09-01 17:41
 **/
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ClusterServiceImpl implements ClusterService {
    private final ClusterDao clusterDao;

    @Override
    public void save(Cluster cluster) {
        if (StringUtils.isEmpty(cluster.getId())) {
            clusterDao.insert(cluster);
        } else {
            clusterDao.updateById(cluster);
        }
    }

    @Override
    public void deleteById(String id) {
        clusterDao.deleteById(id);
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

    @Override
    public PageInfo<Cluster> query4Page(ClusterParams params) {
        PageHelper.startPage(params);
        LambdaQueryWrapper<Cluster> wrapper = Wrappers.lambdaQuery(Cluster.class)
                .like(!StringUtils.isEmpty(params.getName()), Cluster::getName, params)
                .like(!StringUtils.isEmpty(params.getBootstrapServers()), Cluster::getBootstrapServers, params)
                .like(!StringUtils.isEmpty(params.getDescription()), Cluster::getDescription, params)
                .orderByAsc(Cluster::getCreateTime);
        List<Cluster> list = clusterDao.selectList(wrapper);
        return new PageInfo(list);
    }
}
