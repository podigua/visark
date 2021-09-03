package com.podigua.visark.server.option.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.podigua.visark.server.option.entity.Option;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: podigua
 * @create: 2021-09-02 20:40
 **/
@Mapper
public interface OptionDao extends BaseMapper<Option> {
}
