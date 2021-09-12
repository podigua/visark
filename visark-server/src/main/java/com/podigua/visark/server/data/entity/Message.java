package com.podigua.visark.server.data.entity;

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
    private Long offset;
    private String message;
    private Date timestamp;
    private Map<String, String> headers = new HashMap<>(16);

    public static Message create(int partition, long offset, String message, long timestamp, Headers headers) {
        Message result = new Message();
        result.setPartition(partition);
        result.setOffset(offset);
        result.setMessage(message);
        Header[] array = headers.toArray();
        headers.forEach(header -> {
            result.getHeaders().put(header.key(), Arrays.toString(header.value()));
        });
        result.setTimestamp(new Time(timestamp));
        return result;
    }
}
