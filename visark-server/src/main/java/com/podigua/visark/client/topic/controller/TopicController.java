package com.podigua.visark.client.topic.controller;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.podigua.visark.client.topic.entity.TopicOptionProperty;
import com.podigua.visark.core.LabelValuePair;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.kafka.common.serialization.BytesDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

/**
 * @author: podigua
 * @create: 2021-09-09 10:51
 **/
public class TopicController {
    private final String id;
    private final String topic;


    public TopicController(String id, String topic) {
        this.id = id;
        this.topic = topic;
    }


    public Parent getParent() {
        VBox root = new VBox();
        TabPane pane = new TabPane();
        Tab optionTab = getOptionTab();
        pane.getTabs().add(optionTab);
        Tab dataTab = getDataTab();
        pane.getTabs().add(dataTab);
        return root;
    }

    private Tab getDataTab() {
        Tab tab = new Tab();
        VBox root=new VBox();


        tab.setGraphic(root);
        return tab;
    }

    private Tab getOptionTab() {
        Tab tab = new Tab();
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_RIGHT);
        Button save = new Button("保存");
        return tab;
    }


    public List<LabelValuePair> getDeserializers() {
        List<LabelValuePair> result = new ArrayList<>();
        result.add(LabelValuePair.of(BytesDeserializer.class.getName(), "Byte"));
        result.add(LabelValuePair.of(StringDeserializer.class.getName(), "String"));
        return result;
    }


}
