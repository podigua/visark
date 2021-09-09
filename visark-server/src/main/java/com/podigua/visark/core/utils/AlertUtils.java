package com.podigua.visark.core.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

/**
 * @author: podigua
 * @create: 2021-09-07 14:35
 **/
public class AlertUtils {
    /**
     * 提示
     *
     * @param content
     * @return
     */
    public static Alert confirm(String content) {
        Alert alert = new Alert(AlertType.NONE, content, ButtonType.CANCEL, ButtonType.OK);
        return alert;
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
