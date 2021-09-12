package com.podigua.visark.server.cluster.controller;

import com.github.pagehelper.PageInfo;
import com.podigua.visark.core.annotation.WebResult;
import com.podigua.visark.server.cluster.entity.Cluster;
import com.podigua.visark.server.cluster.params.ClusterParams;
import com.podigua.visark.server.cluster.service.ClusterService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: podigua
 * @create: 2021-09-01 17:43
 **/
@RestController
@RequestMapping("/cluster")
@AllArgsConstructor(onConstructor = @__(@Autowired))
@WebResult
public class ClusterRestController {
    private final ClusterService clusterService;

    /**
     * 保存
     *
     * @param cluster
     */
    @PostMapping
    public void save(@RequestBody Cluster cluster) {
        clusterService.save(cluster);
    }

    /**
     * 根据ID删除
     */
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable String id) {
        clusterService.deleteById(id);
    }

    /**
     * 获取全部
     *
     * @return
     */
    @GetMapping("/list")
    public List<Cluster> query4List() {
        return clusterService.query4List();
    }

    /**
     * 分页查询
     *
     * @return
     */
    @GetMapping("/page")
    public PageInfo<Cluster> query4Page(ClusterParams params) {
        return clusterService.query4Page(params);
    }
}
