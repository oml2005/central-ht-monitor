package com.octaviomunoz.centralhtmonitor;

import org.apache.kafka.clients.consumer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class CentralMonitoringService {
    private static final Logger logger = LoggerFactory.getLogger(CentralMonitoringService.class);
    private static final double TEMP_LIMIT = 35.0;
    private static final double HUMIDITY_LIMIT = 50.0;

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "monitor-group");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("warehouse-data"));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                processAndLog(record.value());
            }
        }
    }

    private static void processAndLog(String rawData) {
        try {
            String[] parts = rawData.split(";");
            String id = parts[0].split("=")[1].trim();
            double value = Double.parseDouble(parts[1].split("=")[1].trim());

            if (id.startsWith("t") && value > TEMP_LIMIT) {
                System.out.println("[ALARM] Temp High: " + id + " reading " + value + "°C");
                logger.error("ALARM - Temperature Threshold Exceeded! Sensor: {}, Value: {}°C", id, value);
            } else if (id.startsWith("h") && value > HUMIDITY_LIMIT) {
                System.out.println("[ALARM] Humidity High: " + id + " reading " + value + "%");
                logger.error("ALARM - Humidity Threshold Exceeded! Sensor: {}, Value: {}%", id, value);
            } else {
                System.out.println("Normal log: " + id + " = " + value);
                logger.info("Measurement received: {} = {}", id, value);
            }
        } catch (Exception e) {
            logger.warn("Received malformed data: {}", rawData);
        }
    }
}