package com.demo.flink.service;

import com.demo.flink.constants.SourceConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

/**
 * This class is used to create a required source based on the user input.
 */
@Slf4j
@Service
public class FlinkSourceFactory {

    /**
     * <p>This method is used to create a flink source based on the user input via argument list.</p>
     *
     * @param sourceName
     * @return
     */
    public IFlinkSource createFlinkSource(String sourceName) {
        if (ObjectUtils.isEmpty(sourceName)) return null;

        switch (sourceName) {
            case SourceConstants.FILES:
                return new FileSource();
            case SourceConstants.KAFKA:
                return new KafkaSource();
            case SourceConstants.S3:
                return new S3Source();
            case SourceConstants.DB:
                return new DataBaseSource();
            default:
                log.error("Flink source name is not exists.");
                throw new IllegalArgumentException("source doesn't exists");
        }
    }
}
