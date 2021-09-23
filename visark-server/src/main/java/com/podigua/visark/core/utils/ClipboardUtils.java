package com.podigua.visark.core.utils;

import javafx.application.Platform;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

/**
 * @author: podigua
 * @create: 2021-09-15 16:03
 **/
public class ClipboardUtils {
    /**
     * 复制
     * @param text 待复制文本
     */
    public static void copy(String text){
        Platform.runLater(()->{
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putString(text);
            clipboard.setContent(clipboardContent);
        });
    }
}
