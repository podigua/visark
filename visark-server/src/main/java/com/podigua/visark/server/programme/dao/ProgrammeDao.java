package com.podigua.visark.server.programme.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.podigua.visark.server.programme.entity.Programme;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: podigua
 * @create: 2021-09-17 15:42
 **/
@Mapper
public interface ProgrammeDao extends BaseMapper<Programme> {
}
