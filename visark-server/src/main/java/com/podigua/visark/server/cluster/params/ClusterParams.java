package com.podigua.visark.server.cluster.params;

import com.podigua.visark.core.PageParams;
import lombok.Data;

/**
 * @author: podigua
 * @create: 2021-09-01 17:58
 **/
@Data
public class ClusterParams extends PageParams {
    /**
     * 名称
     */
    private String name;
    /**
     * 地址
     */
    private String bootstrapServers;
    /**
     * 描述
     */
    private String description;
}
