package com.demo.flink.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "event_entity")
public class EventEntity extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "device_id")
    private String deviceId;
    private String modelId;
    private String version;
    private java.sql.Date timestamp;
    private Map<String, String> payload;

    public EventEntity(String deviceId, String modelId, String version, Date timestamp, Map<String, String> payload) {
        this.deviceId = deviceId;
        this.modelId = modelId;
        this.version = version;
        this.timestamp = timestamp;
        this.payload = payload;
    }
}
