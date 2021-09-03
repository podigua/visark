package com.podigua.visark.window.controller;

import com.podigua.visark.window.entity.Window;
import com.podigua.visark.window.service.WindowService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author: podigua
 * @create: 2021-09-02 13:31
 **/
@RestController
@RequestMapping("/window")
@AllArgsConstructor
public class WindowController {
    private final WindowService windowService;

    /**
     * 打开集群配置
     */
    @PostMapping("/open")
    public void open(@RequestBody Window window) {
        windowService.open(window);
    }

}
