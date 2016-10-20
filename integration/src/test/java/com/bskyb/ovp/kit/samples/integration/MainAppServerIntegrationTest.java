package com.bskyb.ovp.kit.samples.integration;

import com.bskyb.ovp.kafka.driver.HttpKafkaDriver;
import com.bskyb.ovp.kafka.driver.KafkaDriver;
import com.google.common.collect.ImmutableList;
import org.junit.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MainAppServerIntegrationTest {

    public static final String URL_UNDER_TEST = "http://localhost:8082/messages";
    private final KafkaDriver kafkaDriver = new HttpKafkaDriver(URI.create("http://localhost:8085"));
    public static final String TOPIC_NAME = "theTopicName";

    @Before
    public  void setupKafka() {
        kafkaDriver.startup();
        kafkaDriver.createTopic(TOPIC_NAME);
    }
    @After
    public void shutdownKafka() {
        kafkaDriver.shutdown();
    }

    @Test
    public void sendMessage_shouldSendMessageToKafka() {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity response = restTemplate.postForEntity(URL_UNDER_TEST,
                ImmutableList.of("One Message", "Second Message").asList(), ResponseEntity.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        List<String> messagesSentToKafka = kafkaDriver.waitForMessages(TOPIC_NAME);
        assertThat(messagesSentToKafka).containsOnly("One Message", "Second Message");
    }

    @Test(expected = HttpServerErrorException.class)
    public void sendMessage_shouldFailIfKafkaIsNotAvailable() {
        kafkaDriver.shutdown();
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.postForEntity(URL_UNDER_TEST,
                ImmutableList.of("One Message", "Second Message").asList(), ResponseEntity.class);

    }



}
