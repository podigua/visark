package com.podigua.visark;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: podigua
 * @create: 2021-09-01 15:51
 **/
@RestController
public class TestController {
    @GetMapping("/test")
    public String test() {
        return "OK";
    }
}
