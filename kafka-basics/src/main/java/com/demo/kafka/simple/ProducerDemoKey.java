package com.demo.kafka.simple;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * @author Sergii Biliaiev
 * Created on 24/02/2020.
 */
public class ProducerDemoKey {
    private static final Logger logger = LoggerFactory.getLogger(ProducerDemoKey.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        String bootstrapServer = "127.0.0.1:9092";

        //Producer properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        //new producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        for (int i = 0; i < 10; i++) {

            String topic = "demo_topic";
            String message = "hello, message " + i;
            String key = "id_" + i;
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, message);
            logger.info("Key: [{}]", key);
            //send test data
            producer.send(record, (recordMetadata, e) -> {
                //execution every time a record is successfully
                if (e == null) {
                    logger.info("Received new metadata. \nTopic: {}\nPartition: {}\nOffset: {}\nTimestamp: {}",
                            recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset(), recordMetadata.timestamp());
                    logger.info("=================================");
                } else {
                    e.printStackTrace();
                    logger.error("Error while producing", e);
                }
            }).get();   //TODO block the send(), for example.
        }

        producer.flush();
        producer.close();
    }
}
