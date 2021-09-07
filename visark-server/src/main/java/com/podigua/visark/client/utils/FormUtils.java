package com.podigua.visark.client.utils;

import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.podigua.javafx.State;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @author: podigua
 * @create: 2021-09-07 14:11
 **/
public class FormUtils {
    private final static double DEFAULT_INSET = 5;

    /**
     * 创建表单窗体
     *
     * @param form    窗体
     * @param width   宽度
     * @param height  高度
     * @param handler 处理
     * @return {@link Stage}
     */
    public static Stage create(Stage stage,Form form, double width, double height, EventHandler<ActionEvent> handler) {
        stage.initOwner(State.getInstance().getStage());
        VBox root = new VBox();
        FormRenderer renderer = new FormRenderer(form);
        root.getChildren().add(renderer);
        HBox hbox = new HBox();
        Button cancel = new Button("取消");
        cancel.setOnAction(event -> {
            stage.close();
        });
        cancel.setCancelButton(true);
        Button save = new Button("保存");
        save.setOnAction(handler);
        save.setDefaultButton(true);
        hbox.getChildren().addAll(cancel, save);
        hbox.setAlignment(Pos.CENTER_RIGHT);
        hbox.setPadding(new Insets(DEFAULT_INSET, DEFAULT_INSET, DEFAULT_INSET, 5));
        root.getChildren().add(hbox);
        stage.setScene(new Scene(root, width, height));
        stage.setFullScreen(false);
        stage.setMaximized(false);
        stage.setResizable(false);
        return stage;
    }
}
