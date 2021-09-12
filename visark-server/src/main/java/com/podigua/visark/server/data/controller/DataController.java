package com.podigua.visark.server.data.controller;

import com.podigua.visark.core.annotation.WebResult;
import com.podigua.visark.server.data.service.DataService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.ExecutorService;

/**
 * @author: podigua
 * @create: 2021-09-12 23:06
 **/
@RestController
@RequestMapping("/data")
@AllArgsConstructor(onConstructor = @__(@Autowired))
@WebResult
public class DataController {
    private final DataService dataService;
    private final ExecutorService executorService;
    /**
     * 接收消息
     *
     * @return
     */
    @GetMapping("/{cluster}/{topic}/receive")
    @WebResult(value = false)
    private SseEmitter receive(@PathVariable String cluster, @PathVariable String topic) {
        SseEmitter emitter = new SseEmitter(0L);
        executorService.execute(()->{
            dataService.receive(cluster,topic,emitter);
        });
        return emitter;
    }
}
