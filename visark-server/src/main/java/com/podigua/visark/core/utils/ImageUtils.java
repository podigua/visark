package com.podigua.visark.core.utils;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author: podigua
 * @create: 2021-09-07 11:00
 **/
@Slf4j
public class ImageUtils {
    /**
     * 加载图片
     *
     * @param url    路径
     * @param width  宽度
     * @param height 高度
     * @return ImageView
     */
    public static ImageView load(String url, double width, double height) {
        ImageView view = new ImageView();
        view.setFitWidth(width);
        view.setFitHeight(height);
        Resource resource = new ClassPathResource(url);
        InputStream is;
        try {
            Image image = new Image(resource.getInputStream());
            view.setImage(image);
        } catch (IOException e) {
            log.error("加载图片失败:" + url, e);
            throw new RuntimeException(e);
        }
        return view;
    }
}
