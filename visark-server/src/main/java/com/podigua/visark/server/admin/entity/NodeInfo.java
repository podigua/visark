package com.podigua.visark.server.admin.entity;

import lombok.Data;
import org.apache.kafka.common.Node;

/**
 * @author: podigua
 * @create: 2021-09-12 01:11
 **/
@Data
public class NodeInfo {
    private Integer id;
    private String host;
    private Integer port;
    private String rack;

    /**
     * 创建
     * @param node
     * @return
     */
    public static NodeInfo of(Node node) {
        NodeInfo result = new NodeInfo();
        result.setId(node.id());
        result.setHost(node.host());
        result.setPort(node.port());
        result.setRack(node.rack());
        return result;
    }
}
