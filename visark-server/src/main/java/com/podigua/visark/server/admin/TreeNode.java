package com.podigua.visark.server.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author: podigua
 * @create: 2021-09-01 20:56
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TreeNode {
    /**
     * 类型
     */
    private NodeType type;
    /**
     * 值
     */
    private String value;
    /**
     * 显示值
     */
    private String label;
    /**
     * 子节点
     */
    private List<TreeNode> children;

    /**
     * 创建
     *
     * @param type
     * @param value
     * @param label
     * @return
     */
    public static TreeNode create(NodeType type, String value, String label) {
        return TreeNode.builder().type(type).value(value).label(label).build();
    }
}
