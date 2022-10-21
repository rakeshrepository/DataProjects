package com.demo.flink.model.entity;

import lombok.Data;
import org.springframework.stereotype.Indexed;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity(name = "device_entity")
public class DeviceEntity extends AuditModel{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String deviceId;
    private String location;
    private String type;
}
