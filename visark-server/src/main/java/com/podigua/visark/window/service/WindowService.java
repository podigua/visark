package com.podigua.visark.window.service;

import com.podigua.visark.window.entity.Window;

/**
 * @author: podigua
 * @create: 2021-09-02 13:31
 **/
public interface WindowService {
    /**
     * 打开新窗口
     *
     * @param window
     */
    void open(Window window);
}
