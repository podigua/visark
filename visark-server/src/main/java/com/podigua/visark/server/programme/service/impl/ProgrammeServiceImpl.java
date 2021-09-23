package com.podigua.visark.server.programme.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.podigua.visark.server.programme.dao.ProgrammeDao;
import com.podigua.visark.server.programme.entity.Programme;
import com.podigua.visark.server.programme.service.ProgrammeService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author: podigua
 * @create: 2021-09-17 15:44
 **/
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ProgrammeServiceImpl implements ProgrammeService {
    private final ProgrammeDao programmeDao;

    @Override
    public Programme save(Programme programme) {
        if (StringUtils.isEmpty(programme.getId())) {
            programmeDao.insert(programme);
        } else {
            programmeDao.updateById(programme);
        }
        return programme;
    }

    @Override
    public void deleteById(String id) {
        programmeDao.deleteById(id);
    }

    @Override
    public List<Programme> getList(String cluster, String topic) {
        LambdaQueryWrapper<Programme> wrapper = Wrappers.lambdaQuery(Programme.class)
                .eq(Programme::getClusterId, cluster)
                .eq(Programme::getTopic, topic)
                .orderByDesc(Programme::getUpdateTime);
        return programmeDao.selectList(wrapper);
    }
}
