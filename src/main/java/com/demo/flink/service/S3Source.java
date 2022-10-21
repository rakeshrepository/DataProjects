package com.demo.flink.service;

import com.demo.flink.model.entity.EventEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;
import org.springframework.stereotype.Service;

/**
 * This class is used to define the S3 source details for the flink source to read the required files.
 */
@Service
@Slf4j
public class S3Source implements IFlinkSource {
    /**
     * <p>This method is used to read file from specified s3 locations.</p>
     */
    @Override
    public void chooseSource() {
        log.debug("S3Source chooseSource() START");
        StreamExecutionEnvironment flinkEnv = StreamExecutionEnvironment.getExecutionEnvironment()
                .setRuntimeMode(RuntimeExecutionMode.STREAMING);

        DataStream<String> inputFile = flinkEnv.readTextFile("s3://<bucket>/<endpoint>");
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
        flinkEnv.setStateBackend(new FsStateBackend("s3://****/sid/checkpoint"));
        log.debug("S3Source chooseSource() END");
    }
}
