package com.demo.flink.util;

import com.demo.flink.constants.FileTypeConstants;
import com.demo.flink.model.entity.EventEntity;
import com.demo.flink.repository.IEventEntityRepo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class JsonConverterUtil {

    private IEventEntityRepo eventEntityRepo;

    public void generateJsonDocument() {

        String sourceFilePath = "C:\\Users\\rakesh.gr\\Flink-POC\\flink\\DeviceTestData.json";
        List<EventEntity> eventEntityList = new ArrayList<>();
        try {
            String deviceInfo = new String(Files.readAllBytes(Paths.get(sourceFilePath)));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            EventEntity[] eventInfoArray = gson.fromJson(deviceInfo, EventEntity[].class);

            for (EventEntity event : eventInfoArray) {
                String fileLocation = generateFileLocation(event.getId());
                log.info("Created file location is : {}", fileLocation);
                Writer writer = new FileWriter(fileLocation);
                gson.toJson(event, writer);

                /** event will be added to collection.*/
                eventEntityList.add(event);

                writer.flush();
                writer.close();
            }
            /** collection of events will be written to database.*/
            eventEntityRepo.saveAll(eventEntityList);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String generateFileLocation(String deviceId) {
        String fileBaseLocation = "C:\\Users\\rakesh.gr\\Flink-POC\\flink\\files\\";
        StringBuilder fileLocation = new StringBuilder(fileBaseLocation);
        fileLocation.append(deviceId).append(FileTypeConstants.FILE_TYPE_JSON);
        return fileLocation.toString();
    }

    public static ByteArrayInputStream convertPayloadIntoBlob(Map<String, String> payload) {
        log.debug("RequestLoggerDaoImpl convertPayloadIntoBlob() method START");
        ByteArrayInputStream byteArrayInputStream = null;
        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            ObjectOutputStream output = new ObjectOutputStream(b);
            output.writeObject(payload);
            byteArrayInputStream = new ByteArrayInputStream(b.toByteArray());
        } catch (IOException e) {
            log.error("IOException thrown {} ", e.getMessage());
        }
        log.debug("JsonConverterUtil convertPayloadIntoBlob() method END");
        return byteArrayInputStream;
    }
}
