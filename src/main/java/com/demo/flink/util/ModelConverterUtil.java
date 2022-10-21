package com.demo.flink.util;

import com.demo.flink.model.dto.StreamDTO;
import com.demo.flink.model.entity.EventEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Map;

@Service
public class ModelConverterUtil {

    public EventEntity convert(StreamDTO inputModel) {
        String deviceId = inputModel.getDeviceId();
        String modelId = inputModel.getModelId();
        Date timestamp = inputModel.getTimestamp();
        String version = inputModel.getVersion();
        Map<String, String> payload = inputModel.getPayload();
        return new EventEntity(deviceId, modelId, version, timestamp, payload);
    }
}
