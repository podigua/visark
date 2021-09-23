package com.podigua.visark.server.programme.service;

import com.podigua.visark.server.programme.entity.Programme;

import java.util.List;

/**
 * @author: podigua
 * @create: 2021-09-17 15:42
 **/
public interface ProgrammeService {
    /**
     * 保存方案
     * @param programme
     * @return
     */
    Programme save(Programme programme);

    /**
     * 根据ID删除
     * @param id
     */
    void deleteById(String id);

    /**
     * 获取列表
     * @param cluster
     * @param topic
     * @return
     */
    List<Programme> getList(String cluster,String topic);
}
