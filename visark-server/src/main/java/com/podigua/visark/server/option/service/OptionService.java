package com.podigua.visark.server.option.service;

import com.podigua.visark.server.option.entity.Option;

/**
 * @author: podigua
 * @create: 2021-09-02 20:41
 **/
public interface OptionService {
    /**
     * 保存
     *
     * @param option
     */
    void save(Option option);

    /**
     * 获取
     *
     * @return
     */
    Option get();
}
