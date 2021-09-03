package com.podigua.visark.window.utils;

import com.podigua.javafx.State;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 * @author: podigua
 * @create: 2021-09-02 13:40
 **/
public class WindowUtils {

    /**
     * 创建窗体
     *
     * @param title  标题
     * @param width  宽度
     * @param height 高度
     * @param url    url
     * @return Stage
     */
    public static Stage create(String title, Double width, Double height, String url) {
        Stage stage = new Stage();
        VBox root = new VBox();
        WebView view = createWebView(url);
        root.getChildren().add(view);
        stage.setScene(new Scene(root, width, height));
        stage.initOwner(State.getInstance().getStage());
        view.prefHeightProperty().bind(root.heightProperty());
        view.prefWidthProperty().bind(root.widthProperty());
        stage.setTitle(title);
        stage.setFullScreen(false);
        stage.setResizable(false);
        stage.setMaximized(false);
        return stage;
    }

    /**
     * 创建WebView
     *
     * @param url url
     * @return {@link WebView}
     */
    public static WebView createWebView(String url) {
        WebView view = new WebView();
//        view.setContextMenuEnabled(false);
        WebEngine engine = view.getEngine();
        engine.setJavaScriptEnabled(true);
        engine.load(url);
        return view;
    }
}
