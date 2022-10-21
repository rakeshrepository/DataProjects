package com.demo.flink.model.dto;

import lombok.Data;

import java.sql.Date;
import java.util.Map;

@Data
public class StreamDTO {
    private String id;
    private String deviceId;
    private String modelId;
    private String version;
    private Date timestamp;
    private Map<String, String> payload;
}
