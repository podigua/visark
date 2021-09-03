package com.podigua.visark.server.option.controller;

import com.podigua.visark.core.LabelValuePair;
import com.podigua.visark.core.annotation.WebResult;
import com.podigua.visark.server.option.entity.Option;
import com.podigua.visark.server.option.service.OptionService;
import lombok.AllArgsConstructor;
import org.apache.kafka.common.serialization.BytesDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: podigua
 * @create: 2021-09-02 20:43
 **/
@RestController
@RequestMapping("/option")
@WebResult
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class OptionController {
    private final OptionService optionService;

    /**
     * 反序列化
     *
     * @return
     */
    @GetMapping("/deserializers")
    public List<LabelValuePair> deserializers() {
        List<LabelValuePair> result = new ArrayList<>();
        result.add(LabelValuePair.of(StringDeserializer.class.getName(), "String"));
        result.add(LabelValuePair.of(BytesDeserializer.class.getName(), "Byte"));
        return result;
    }

    /**
     * 保存
     *
     * @param option
     */
    @PostMapping
    public void save(@RequestBody Option option) {
        optionService.save(option);
    }


    /**
     * 获取全局配置
     *
     * @return
     */
    @GetMapping
    public Option get() {
        return optionService.get();
    }
}
