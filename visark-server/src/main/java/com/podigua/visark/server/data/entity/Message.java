package com.podigua.visark.server.data.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;

import java.sql.Time;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: podigua
 * @create: 2021-09-12 23:33
 **/
@Data
public class Message {
    private Integer partition;
    private String offset;
    private String message;
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date timestamp;
    private Map<String, String> headers = new HashMap<>(16);

    public static Message create(int partition, long offset, String message, long timestamp, Headers headers) {
        Message result = new Message();
        result.setPartition(partition);
        result.setOffset(String.valueOf(offset));
        result.setMessage(message);
        headers.forEach(header -> {
            result.getHeaders().put(header.key(), Arrays.toString(header.value()));
        });
        result.setTimestamp(new Date(timestamp));
        return result;
    }
}
