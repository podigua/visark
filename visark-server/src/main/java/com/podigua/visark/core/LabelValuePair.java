package com.podigua.visark.core;

import lombok.Data;

/**
 * @author: podigua
 * @create: 2021-09-02 20:56
 **/
@Data
public class LabelValuePair {
    private String value;
    private String label;

    /**
     * 创建对象
     *
     * @param value value
     * @param label label
     * @return {@link LabelValuePair}
     */
    public static LabelValuePair of(String value, String label) {
        LabelValuePair result = new LabelValuePair();
        result.setValue(value);
        result.setLabel(label);
        return result;
    }
}
