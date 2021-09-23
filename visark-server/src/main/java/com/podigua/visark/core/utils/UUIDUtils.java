package com.podigua.visark.core.utils;

import java.util.UUID;

/**
 * @author: podigua
 * @create: 2021-09-15 19:20
 **/
public class UUIDUtils {
    /**
     * uuid
     *
     * @return
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
