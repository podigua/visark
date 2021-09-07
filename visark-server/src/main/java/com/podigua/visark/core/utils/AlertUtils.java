package com.podigua.visark.core.utils;

import com.podigua.javafx.State;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author: podigua
 * @create: 2021-09-07 14:35
 **/
public class AlertUtils {

    public static void show(Stage parent,String message, Long time) {
        Stage stage = new Stage();
        stage.initOwner(parent);
        stage.initStyle(StageStyle.TRANSPARENT);
        HBox root = new HBox();
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background:transparent;");
        Label label = new Label(message);
        label.setTextFill(Color.GREEN);
        root.getChildren().add(label);
        Scene scene = new Scene(root, 500, 50);
        scene.setFill(null);
        stage.setScene(scene);
        stage.show();
        Platform.runLater(()->{
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                stage.close();
            }
        });
    }

    /**
     * 提示
     *
     * @param content
     * @return
     */
    public static Alert alert(String content) {
        Alert alert = new Alert(AlertType.NONE, content, ButtonType.OK);
        return alert;
    }
}
