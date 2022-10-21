package com.demo.flink.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.springframework.stereotype.Service;

/**
 * This class is used to specify the kafka as source data to read events.
 */
@Service
@Slf4j
public class KafkaSource implements IFlinkSource {

    /**
     * <p>This method is used to choose the kafka consumer to read the events data.</p>
     */
    @Override
    public void chooseSource() {
        log.debug("KafkaSource chooseSource START");
        StreamExecutionEnvironment flinkEnv = StreamExecutionEnvironment.getExecutionEnvironment()
                .setRuntimeMode(RuntimeExecutionMode.STREAMING);
        org.apache.flink.connector.kafka.source.KafkaSource<String> source = org.apache.flink.connector.kafka.source.KafkaSource.<String>builder()
                .setBootstrapServers("bootstrap-server1")
                .setTopics("topic-a", "topic-b")
                .setGroupId("my-group")
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();

        flinkEnv.fromSource(source, WatermarkStrategy.noWatermarks(), "Kafka Source");

        DataStream<String> kafkaStream = null;
        try {
            flinkEnv.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        KafkaSink<String> sink = KafkaSink.<String>builder()
                .setBootstrapServers("brokers")
                .setRecordSerializer(KafkaRecordSerializationSchema.builder()
                        .setTopic("topic-name")
                        .setValueSerializationSchema(new SimpleStringSchema())
                        .build()
                ).setDeliverGuarantee(DeliveryGuarantee.AT_LEAST_ONCE)
                .build();

        kafkaStream.sinkTo(sink);
        log.debug("KafkaSource chooseSource END");
    }
}
