package com.podigua.visark.client.index.entity;

import com.podigua.visark.server.admin.entity.NewTopicEntity;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: podigua
 * @create: 2021-09-08 19:24
 **/
@Data
public class NewTopicEntityProperty {
    private SimpleStringProperty name = new SimpleStringProperty("");
    private SimpleIntegerProperty partitions = new SimpleIntegerProperty(1);
    private SimpleDoubleProperty replication = new SimpleDoubleProperty(1D);
    private SimpleMapProperty<String, String> config = new SimpleMapProperty();

    /**
     * 转换
     *
     * @param entity
     * @return
     */
    public static NewTopicEntityProperty from(NewTopicEntity entity) {
        NewTopicEntityProperty property = new NewTopicEntityProperty();
        property.setName(new SimpleStringProperty(entity.getName()));
        property.setPartitions(new SimpleIntegerProperty(entity.getPartitions()));
        property.setReplication(new SimpleDoubleProperty(entity.getReplication()));
        Map<String, String> config = entity.getConfigs();
        if (!CollectionUtils.isEmpty(config)) {
            config.forEach((key, value) -> {
                property.getConfig().put(key, value);
            });
        }
        return property;
    }

    /**
     * 转换
     *
     * @return
     */
    public NewTopicEntity to() {
        NewTopicEntity result = new NewTopicEntity();
        result.setName(this.getName().get());
        result.setPartitions(this.getPartitions().get());
        result.setReplication(this.getReplication().getValue().shortValue());
        Map<String, String> config = new HashMap<>();
        SimpleMapProperty<String, String> configs = this.getConfig();
        if (!CollectionUtils.isEmpty(configs)) {
            configs.forEach(config::put);
        }
        result.setConfigs(configs);
        return result;
    }
}
