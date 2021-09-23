package com.podigua.visark.server.data.controller;

import com.podigua.visark.core.annotation.WebResult;
import com.podigua.visark.server.data.entity.Message;
import com.podigua.visark.server.data.service.KafkaSizeReceiveService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: podigua
 * @create: 2021-09-15 21:16
 **/
@RestController
@RequestMapping("/data/size")
@WebResult
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DataController {
    private final KafkaSizeReceiveService kafkaSizeReceiveService;

    /**
     * 获取数据
     *
     * @return
     */
    @GetMapping
    public List<Message> getMessageBySize(String cluster, String topic, String offset, Long size) {
        return kafkaSizeReceiveService.start(cluster, topic, offset, size);
    }
}
