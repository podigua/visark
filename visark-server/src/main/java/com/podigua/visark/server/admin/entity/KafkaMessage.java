package com.podigua.visark.server.admin.entity;

import com.podigua.visark.server.programme.entity.Programme;
import lombok.Data;

import java.util.List;

/**
 * @author: podigua
 * @create: 2021-09-24 16:56
 **/
@Data
public class KafkaMessage {
    private String key;
    private String value;
    private List<Programme.Expression> expressions;
}
