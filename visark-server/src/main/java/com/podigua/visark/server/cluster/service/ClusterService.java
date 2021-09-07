package com.podigua.visark.server.cluster.service;

import com.github.pagehelper.PageInfo;
import com.podigua.visark.server.cluster.entity.Cluster;
import com.podigua.visark.server.cluster.params.ClusterParams;

import java.util.List;

/**
 * @author: podigua
 * @create: 2021-09-01 17:40
 **/
public interface ClusterService {
    /**
     * 保存
     *
     * @param cluster
     */
    void save(Cluster cluster);

    /**
     * 根据ID删除
     *
     * @param id
     */
    void deleteById(String id);

    /**
     * 根据ID获取
     *
     * @param id
     * @return
     */
    Cluster getById(String id);

    /**
     * 获取所有集群配置
     *
     * @return
     */
    List<Cluster> query4List();
}
