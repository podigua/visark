package com.podigua.visark.window.entity;

import lombok.Data;

/**
 * @author: podigua
 * @create: 2021-09-02 14:08
 **/
@Data
public class Window {
    /**
     * 标题
     */
    private String title;
    /**
     * 路径
     */
    private String path;
    /**
     * 宽度
     */
    private Double width;
    /**
     * 高度
     */
    private Double height;
}
