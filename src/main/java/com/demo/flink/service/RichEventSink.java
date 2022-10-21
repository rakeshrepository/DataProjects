package com.demo.flink.service;

import com.demo.flink.model.entity.EventEntity;
import com.demo.flink.util.JsonConverterUtil;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

@Service
public class RichEventSink extends RichSinkFunction<EventEntity> {
    private static final String INSERT_CASE = "INSERT INTO public.event_entity (deviceId, modelId,version,timestamp,payload) "
            + "VALUES (?, ?, ?, ?,?) ";

    private PreparedStatement statement;

    public void insertEvent(EventEntity event) throws Exception {

        //statement.setString(1, event.getId());
        statement.setString(2, event.getDeviceId());
        statement.setString(3, event.getModelId());
        statement.setString(4, event.getVersion());
        statement.setDate(5, event.getTimestamp());
        statement.setBlob(6, JsonConverterUtil.convertPayloadIntoBlob(event.getPayload()));
        statement.addBatch();
        statement.executeBatch();
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        Class.forName("org.postgresql.Driver");
        Connection connection =
                DriverManager.getConnection("jdbc:postgresql://localhost:49153/flinkdatabase?user=postgres&password=postgrespw");
        statement = connection.prepareStatement(INSERT_CASE);
    }

}
