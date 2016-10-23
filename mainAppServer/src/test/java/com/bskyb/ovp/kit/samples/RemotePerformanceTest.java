package com.bskyb.ovp.kit.samples;

import com.bskyb.ovp.kafka.driver.HttpKafkaDriver;
import com.bskyb.ovp.kafka.driver.KafkaDriver;
import com.bskyb.ovp.kafka.driver.KafkaDriverStartResetRule;
import com.bskyb.ovp.kafka.driver.KafkaDriverStartStopRule;
import org.junit.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@Ignore
public class RemotePerformanceTest {

    private static final String TOPIC_NAME = "theTopicName";
    protected static final String URL_UNDER_TEST = "http://localhost:8083/messages";

    private final static KafkaDriver remoteKafkaDriver = new HttpKafkaDriver(URI.create("http://localhost:8085"));

    @ClassRule
    public static KafkaDriverStartStopRule startStopRule = new KafkaDriverStartStopRule(remoteKafkaDriver);

    @Rule
    public KafkaDriverStartResetRule startResetRule = new KafkaDriverStartResetRule(remoteKafkaDriver);

    @BeforeClass
    public static void createTopic() throws Exception {
        remoteKafkaDriver.createTopic(TOPIC_NAME);
    }

    @Test
    public void sendMessages() {
        Integer iterations = Integer.valueOf(100000);
        IntStream.rangeClosed(1, iterations).forEach(i->callApp("message one","message two"));

        assertThat(remoteKafkaDriver.waitForMessages(TOPIC_NAME).size()).isEqualTo(iterations*2);

    }
    private void callApp(String... messages) {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity response = restTemplate.postForEntity(URL_UNDER_TEST,
                messages, ResponseEntity.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }
}
