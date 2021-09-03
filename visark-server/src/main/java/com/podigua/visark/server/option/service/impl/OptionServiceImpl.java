package com.podigua.visark.server.option.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.podigua.visark.server.option.dao.OptionDao;
import com.podigua.visark.server.option.entity.Option;
import com.podigua.visark.server.option.service.OptionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * @author: podigua
 * @create: 2021-09-02 20:42
 **/
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class OptionServiceImpl implements OptionService {
    private final OptionDao optionDao;

    @Override
    public synchronized void save(Option option) {
        if (StringUtils.isEmpty(option.getId())) {
            optionDao.insert(option);
        } else {
            optionDao.updateById(option);
        }

    }

    @Override
    public Option get() {
        return Optional.ofNullable(optionDao.selectOne(Wrappers.emptyWrapper())).orElse(new Option());
    }
}
