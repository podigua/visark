package com.podigua.visark;

import com.podigua.javafx.application.AbstractJavafxApplication;
import com.podigua.visark.core.properties.VisarkProperties;
import com.podigua.visark.window.utils.WindowUtils;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

/**
 * @author: podigua
 * @create: 2021-08-31 17:36
 **/
@SpringBootApplication
public class VisarkApplication extends AbstractJavafxApplication {
    @Autowired
    private VisarkProperties visarkProperties;

    public static void main(String[] args) {
        launch(VisarkApplication.class, args);
    }


    @SneakyThrows
    @Override
    protected void ready(Stage stage) {
        HBox root = new HBox();
        WebView view = WindowUtils.createWebView(visarkProperties.getUrl());
        root.getChildren().add(view);
        stage.setScene(new Scene(root,1000,750));
        view.prefHeightProperty().bind(root.heightProperty());
        view.prefWidthProperty().bind(root.widthProperty());
        stage.setTitle("imkt");
        stage.getIcons().add(new Image(new ClassPathResource("images/logo.png").getInputStream()));

        stage.show();
    }


}
