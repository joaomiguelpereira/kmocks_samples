package com.bskyb.ovp.kit.samples;

import com.bskyb.ovp.kafka.driver.KafkaDriver;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public abstract class FunctionalTest {

    protected static final String URL_UNDER_TEST = "http://localhost:8083/messages";
    protected static final String TOPIC_NAME = "theTopicName";

    @Test
    public void addMessages_shouldSendMessagesToKafka() {
        //when
        callApp("Message One", "Message two");

        //then
        List<String> messagesSentToKafka = kafkaDriver().waitForMessages(TOPIC_NAME);
        assertThat(messagesSentToKafka).containsOnly("Message One", "Message two");
    }

    @Test
    public void addMessages_shouldSendMoreMessagesToKafka() {
        //when
        callApp("First Message", "Second Message");

        //then
        List<String> messagesSentToKafka = kafkaDriver().waitForMessages(TOPIC_NAME);
        assertThat(messagesSentToKafka).containsOnly("First Message", "Second Message");
    }

    @Before
    public void resetState() {
        kafkaDriver().reset();
    }

    protected abstract KafkaDriver kafkaDriver();

    private void callApp(String... messages) {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity response = restTemplate.postForEntity(URL_UNDER_TEST,
                messages, ResponseEntity.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

}
