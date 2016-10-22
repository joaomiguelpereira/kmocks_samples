package com.bskyb.ovp.kit.samples;

import com.bskyb.ovp.kafka.driver.HttpKafkaDriver;
import com.bskyb.ovp.kafka.driver.KafkaDriver;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.net.URI;

public class RemoteFunctionalTest extends FunctionalTest {

    //Create new Http Driver connecting to 8085
    private final static KafkaDriver remoteKafkaDriver = new HttpKafkaDriver(URI.create("http://localhost:8085"));

    @BeforeClass
    public static void startKafka() throws Exception {
        FunctionalTestHelper.startKafkaAndCreateTopic(remoteKafkaDriver, TOPIC_NAME);
    }

    @AfterClass
    public static void stopKafka() throws Exception {
        FunctionalTestHelper.shutDownKafka(remoteKafkaDriver);
    }

    @Override
    protected KafkaDriver kafkaDriver() {
        return remoteKafkaDriver;
    }
}
