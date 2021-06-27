package com.demo.kafka.simple;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

/**
 * @author Sergii Biliaiev
 * Created on 25/02/2020.
 */
public class ConsumerDemoWithThread {
    private static final Logger logger = LoggerFactory.getLogger(ConsumerDemoWithThread.class.getName());

    public static void main(String[] args) {
        new ConsumerDemoWithThread().run();
    }

    private ConsumerDemoWithThread() {
    }

    public void run() {
        final String bootstrapServers = "127.0.0.1:9092";
        final String groupId = "my-six-application";
        final String topic = "first_topic";

        CountDownLatch latch = new CountDownLatch(1);
        logger.info("Creating the consumer thread");
        ConsumerRunnable myConsumer = new ConsumerRunnable(latch, topic, bootstrapServers, groupId);

        Thread myThread = new Thread(myConsumer);
        myThread.start();

        //add shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Caught shutdown hook");
            myConsumer.shutdown();
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logger.info("Application has exited");
        }));

        try {
            latch.await();
        } catch (InterruptedException e) {
            logger.error("Application is interrupted", e);
        } finally {
            logger.info("Application is closing");
        }
    }

    public class ConsumerRunnable implements Runnable {

        private final Logger logger = LoggerFactory.getLogger(ConsumerRunnable.class.getName());
        private final CountDownLatch latch;
        private final KafkaConsumer<String, String> consumer;

        public ConsumerRunnable(CountDownLatch latch, String topic, String bootstrapServers, String groupId) {
            this.latch = latch;//create consumer

            Properties properties = new Properties();
            properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
            properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

            consumer = new KafkaConsumer<>(properties);
            //        consumer.subscribe(Collections.singleton(topic));
            consumer.subscribe(Arrays.asList(topic));

        }

        @Override
        public void run() {
            //poll for new data
            try {
                while (true) {
                    ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(100));
                    for (ConsumerRecord<String, String> record : consumerRecords) {
                        this.logger.info("Key: [{}], Value: [{}]", record.key(), record.value());
                        this.logger.info("Partition [{}], Offset [{}]", record.partition(), record.offset());
                    }
                }
            } catch (WakeupException we) {
                this.logger.info("Received shutdown signal!");
            } finally {
                consumer.close();
                //tell main code - we are done with the consumer
                latch.countDown();
            }
        }

        public void shutdown() {
            //the wakeup method is a special method to interrupt consumer.poll()
            //it will throw the exception WakeUpException
            consumer.wakeup();
        }
    }
}
