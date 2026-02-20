package com.octaviomunoz.centralhtmonitor;

import org.apache.kafka.clients.producer.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WarehouseService {
    private static final Logger logger = LoggerFactory.getLogger(CentralMonitoringService.class);
    private static final String TOPIC = "warehouse-data";

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        // Listen for Temperature (3344) and Humidity (3355)
        startUdpListener(3344, producer);
        startUdpListener(3355, producer);
    }

    private static void startUdpListener(int port, KafkaProducer<String, String> producer) {
        new Thread(() -> {
            try (DatagramSocket socket = new DatagramSocket(port)) {
                System.out.println("Warehouse listening for UDP on port " + port);
                logger.info("Warehouse listening for UDP on port " + port);
                byte[] buffer = new byte[1024];
                while (true) {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    String msg = new String(packet.getData(), 0, packet.getLength()).trim();

                    // Push to Kafka
                    producer.send(new ProducerRecord<>(TOPIC, msg));
                }
            } catch (Exception e) { e.printStackTrace(); }
        }).start();
    }
}