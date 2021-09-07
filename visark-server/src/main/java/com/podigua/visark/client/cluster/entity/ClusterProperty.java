package com.podigua.visark.client.cluster.entity;

import com.podigua.visark.server.cluster.entity.Cluster;
import javafx.beans.property.SimpleStringProperty;
import lombok.Data;

/**
 * @author: podigua
 * @create: 2021-09-07 11:36
 **/
@Data
public class ClusterProperty {
    private SimpleStringProperty id = new SimpleStringProperty("");
    private SimpleStringProperty name = new SimpleStringProperty("");
    private SimpleStringProperty bootstrapServers = new SimpleStringProperty("");
    private SimpleStringProperty description = new SimpleStringProperty("");

    /**
     * 创建
     *
     * @param cluster
     * @return
     */
    public static ClusterProperty from(Cluster cluster) {
        ClusterProperty property = new ClusterProperty();
        property.setName(new SimpleStringProperty(cluster.getName()));
        property.setId(new SimpleStringProperty(cluster.getId()));
        property.setBootstrapServers(new SimpleStringProperty(cluster.getBootstrapServers()));
        property.setDescription(new SimpleStringProperty(cluster.getDescription()));
        return property;
    }

    public  Cluster to(){
        Cluster cluster=new Cluster();
        cluster.setId(this.getId().get());
        cluster.setName(this.getName().get());
        cluster.setBootstrapServers(this.getBootstrapServers().get());
        cluster.setDescription(this.getDescription().get());
        return cluster;
    }

}
