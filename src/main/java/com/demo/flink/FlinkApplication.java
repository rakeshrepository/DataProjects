package com.demo.flink;

import com.demo.flink.constants.SourceConstants;
import com.demo.flink.service.FlinkSourceFactory;
import com.demo.flink.util.JsonConverterUtil;
import org.apache.flink.api.java.utils.ParameterTool;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FlinkApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlinkApplication.class, args);
        ParameterTool param = ParameterTool.fromArgs(args);

        /** Json array file converted into multiple documents */
        JsonConverterUtil converterUtil = new JsonConverterUtil();
        converterUtil.generateJsonDocument();

        FlinkSourceFactory flinkSourceFactory = new FlinkSourceFactory();
        String inputSrc = param.get(SourceConstants.SOURCE, SourceConstants.FILES);
        flinkSourceFactory.createFlinkSource(inputSrc);
    }

}
