package com.podigua.visark;

import com.podigua.javafx.application.AbstractJavafxApplication;
import com.podigua.javafx.support.FxmlService;
import com.podigua.javafx.support.SplashScreen;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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
    private FxmlService fxmlService;

    public static void main(String[] args) {
        launch(VisarkApplication.class,new SplashScreen(){

            @Override
            public boolean visible() {
                return false;
            }

            @Override
            public String getImagePath() {
                return null;
            }
        },args);
    }


    @SneakyThrows
    @Override
    protected void ready(Stage stage) {
        Parent parent = fxmlService.load("fxml/index.fxml");
        stage.setScene(new Scene(parent,1000,750));
        stage.setTitle("imkt");
        stage.getIcons().add(new Image(new ClassPathResource("images/logo.png").getInputStream()));
        stage.show();
    }


}
