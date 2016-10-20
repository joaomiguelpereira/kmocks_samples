package com.bskyb.ovp.kit.samples;

import com.bskyb.ovp.kafka.driver.KafkaDriver;
import com.bskyb.ovp.kafka.driver.KafkaDriverBuilder;
import com.bskyb.ovp.kit.samples.mainappserver.SimpleServer;
import com.google.common.collect.ImmutableList;
import org.junit.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class SimpleServerFunctionalTest {

    public static final String URL_UNDER_TEST = "http://localhost:8083/messages";

    private static final KafkaDriver kafkaDriver = new KafkaDriverBuilder().brokerPort(9093).build();
    public static final String TOPIC_NAME = "theTopicName";

    @BeforeClass
    public static void startKafkaAndCreateTopic() throws Exception {
        kafkaDriver.startup();
        assertThat(kafkaDriver.isKafkaRunning());
        kafkaDriver.createTopic(TOPIC_NAME);
        assertThat(kafkaDriver.listTopics()).contains(TOPIC_NAME);

        SimpleServer.main();
    }

    @AfterClass
    public static void shutDownKafka() {
        kafkaDriver.shutdown();
    }

    @Test
    public void addMessages_shouldSendMessagesToKafka() {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity response = restTemplate.postForEntity(URL_UNDER_TEST,
                ImmutableList.of("One Message", "Second Message").asList(), ResponseEntity.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        List<String> messagesSentToKafka = kafkaDriver.waitForMessages(TOPIC_NAME);
        assertThat(messagesSentToKafka).containsOnly("One Message", "Second Message");

    }
}
