package com.bskyb.ovp.kit.sample.mainapp;


import com.bskyb.ovp.kafka.driver.KafkaDriver;
import com.bskyb.ovp.kafka.driver.KafkaDriverBuilder;
import com.bskyb.ovp.kafka.driver.KafkaDriverStartResetRule;
import com.bskyb.ovp.kafka.driver.KafkaDriverStartStopRule;
import org.junit.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MainApplicationTest {

    private static final String[] listOfMessages = {
            "A very interesting message",
            "Another message for kafka to consume",
            "I'm the last message"};

    private static final String[] secondListOfMessages = {
            "Another very interesting message",
            "Yet Another message for kafka to consume",
            "I'm the last message, you see me?"};
    public static final String TOPIC = "theTopic";


    //System under test
    private MainApplication sut;

    //Creates a default driver with kafka listening in localhost:5001 and zookeeper listening in localhost:5000
    private static KafkaDriver kafkaDriver = new KafkaDriverBuilder().build();

    @ClassRule
    public static KafkaDriverStartStopRule classRule = new KafkaDriverStartStopRule(kafkaDriver);

    @Rule
    public KafkaDriverStartResetRule rule = new KafkaDriverStartResetRule(kafkaDriver);


    @BeforeClass
    public static void startKafka() {
        kafkaDriver.createTopic(TOPIC);
        assertThat(kafkaDriver.listTopics()).contains(TOPIC);
    }

    @Before
    public void setupSUT() {
        sut = new MainApplication("localhost:5001", TOPIC);
    }

    @Test
    public void sendSomeMessages_shouldSendMessageToKafka() {
        //when
        sut.sendSomeMessages(listOfMessages);

        //then
        List<String> receivedMessages = kafkaDriver.waitForMessages(TOPIC);

        assertThat(receivedMessages).containsOnly(listOfMessages);

    }

    @Test
    public void sendSomeMessages_shouldSendMoreMessageToKafka() {
        //when
        sut.sendSomeMessages(secondListOfMessages);

        //then
        List<String> receivedMessages = kafkaDriver.waitForMessages(TOPIC);

        assertThat(receivedMessages).containsOnly(secondListOfMessages);

    }

}