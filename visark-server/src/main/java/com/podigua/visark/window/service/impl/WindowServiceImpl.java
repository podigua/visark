package com.podigua.visark.window.service.impl;

import com.podigua.visark.core.properties.VisarkProperties;
import com.podigua.visark.window.entity.Window;
import com.podigua.visark.window.service.WindowService;
import com.podigua.visark.window.utils.WindowUtils;
import javafx.application.Platform;
import javafx.stage.Stage;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: podigua
 * @create: 2021-09-02 13:33
 **/
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class WindowServiceImpl implements WindowService {
    private final VisarkProperties visarkProperties;

    @Override
    public void open(Window window) {
        Platform.runLater(() -> {
            Stage stage = WindowUtils.create(window.getTitle(), window.getWidth(), window.getHeight(), visarkProperties.getUrl() + window.getPath());
            stage.show();
        });
    }
}
