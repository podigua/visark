package com.podigua.visark.core;

import com.github.pagehelper.IPage;
import lombok.Data;

/**
 * @author podigua
 */
@Data
public class PageParams implements IPage {
    /**
     * 页码
     */
    protected Integer pageNum;
    /**
     * 页大小
     */
    protected Integer pageSize;
    /**
     * 排序
     */
    protected String orderBy;
}
