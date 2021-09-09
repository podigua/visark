package com.podigua.visark.client.topic.entity;

import javafx.beans.property.SimpleStringProperty;
import lombok.Data;

/**
 * @author: podigua
 * @create: 2021-09-09 11:03
 **/
@Data
public class TopicOptionProperty {
    /**
     * key 反序列化
     */
    private SimpleStringProperty keyDeserializer = new SimpleStringProperty("");
    /**
     * value 反序列化
     */
    private SimpleStringProperty valueDeserializer = new SimpleStringProperty("");
}
