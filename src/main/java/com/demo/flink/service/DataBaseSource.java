package com.demo.flink.service;

import com.demo.flink.model.entity.EventEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;
import org.springframework.stereotype.Service;

/**
 * This class is used to read the records from database, and it is source for flink.
 */
@Service
@Slf4j
public class DataBaseSource implements IFlinkSource {
    /**
     * <p>This method is used to read the records from the database.</p>
     */
    @Override
    public void chooseSource() {
        log.debug("DataBaseSource chooseSource() START");
        String folderPath = "C:\\Users\\rakesh.gr\\Flink-POC\\flink\\files";
        StreamExecutionEnvironment flinkEnv = StreamExecutionEnvironment.getExecutionEnvironment()
                .setRuntimeMode(RuntimeExecutionMode.STREAMING);
        DataStream<String> inputFile = flinkEnv.readTextFile(folderPath);

        DataStream<EventEntity> eventStream = inputFile.flatMap(new RichFlatMapFunction<String, EventEntity>() {
            @Override
            public void flatMap(String value, Collector<EventEntity> out) throws Exception {
                ObjectMapper objectMapper = new ObjectMapper();
                out.collect(objectMapper.readValue(value, EventEntity.class));
            }
        });

        eventStream.addSink(new RichEventSink());
        try {
            flinkEnv.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.debug("DataBaseSource chooseSource() END");
    }
}
