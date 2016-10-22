package com.bskyb.ovp.kit.sample.mainapp;

import com.google.common.base.Verify;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.*;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import static java.text.MessageFormat.format;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.MAX_BLOCK_MS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;



@Slf4j
public class ProducerComponent {

    private final String brokerUri;
    private final String topic;

    private static final long TIMEOUT_3_SECONDS = SECONDS.toMillis(3);

    public ProducerComponent(String brokerUri, String topic) {
        Verify.verify(StringUtils.isNotEmpty(brokerUri), "Broker URI cannot be null or empty");
        Verify.verify(StringUtils.isNotEmpty(topic), "Topic cannot be null or empty");
        this.brokerUri = brokerUri;
        this.topic = topic;
    }


    public void sendSomeMessages(String...messages) {
        log.info("Sending some messages");

        ProducerRecord[] producerRecords = Stream.of(messages)
                .map(msgBody -> new ProducerRecord<String, String>(topic, msgBody))
                .toArray(ProducerRecord[]::new);


        Producer<String, String> producer = createProducer(new Properties(), String.class, String.class);

        try {
            for (ProducerRecord<String, String> msg : producerRecords) {
                log.info("Sending message: {} to topic {}", msg.value(), msg.topic());
                RecordMetadata recordMetadata = producer.send(msg).get();
                log.info("Sent message: {} -> topic: {} - offset: {}", msg.value(), recordMetadata.topic(), recordMetadata.offset());
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("Sending messages", e);
            throw new MainApplicationException(e);
        } finally {
            producer.close(1, SECONDS);
        }
    }

    private Producer<String, String> createProducer(Properties props, Class<String> keyType, Class<String> valueType) {
        props.put(KEY_SERIALIZER_CLASS_CONFIG, format("org.apache.kafka.common.serialization.{0}Serializer", keyType.getSimpleName()));
        props.put(VALUE_SERIALIZER_CLASS_CONFIG, format("org.apache.kafka.common.serialization.{0}Serializer", valueType.getSimpleName()));
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerUri);
        props.put(MAX_BLOCK_MS_CONFIG, TIMEOUT_3_SECONDS);
        return new KafkaProducer<>(props);
    }

}
