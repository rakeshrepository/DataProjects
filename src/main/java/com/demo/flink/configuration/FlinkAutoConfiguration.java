package com.demo.flink.configuration;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@Configuration
@EnableConfigurationProperties(FlinkProperties.class)
public class FlinkAutoConfiguration {

    @Bean("flinkEnvironment")
    StreamExecutionEnvironment getFlinkEnvironment(FlinkProperties flinkProperties) {
        return StreamExecutionEnvironment.getExecutionEnvironment();
    }
}
