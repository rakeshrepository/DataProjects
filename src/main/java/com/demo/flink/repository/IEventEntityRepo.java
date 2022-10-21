package com.demo.flink.repository;

import com.demo.flink.model.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEventEntityRepo extends JpaRepository<EventEntity, String> {
}
