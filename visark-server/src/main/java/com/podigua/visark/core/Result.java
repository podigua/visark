package com.podigua.visark.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author podigua
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Result<T> {
    public static String SUCCESS = "0";
    public static String OK = "OK";
    private String status;
    private T data;
    private String message;
    private Long timestamp;


    public static Result ok(Object data) {
        return Result.builder()
                .status(SUCCESS)
                .timestamp(System.currentTimeMillis())
                .data(data)
                .message(OK)
                .build();
    }

}
