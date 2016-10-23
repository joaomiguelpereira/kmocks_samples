package com.bskyb.ovp.kit.samples;

import com.bskyb.ovp.kafka.driver.*;
import com.bskyb.ovp.kit.samples.mainappserver.SimpleServer;
import org.junit.*;

import java.net.URI;

@Ignore
public class RemoteFunctionalWithRuleTest extends FunctionalTest {

    //Create new Local Driver listening on port 9093
    private final static KafkaDriver remoteKafkaDriver = new HttpKafkaDriver(URI.create("http://localhost:8085"));

    @ClassRule
    public static KafkaDriverStartStopRule startStopRule = new KafkaDriverStartStopRule(remoteKafkaDriver);

    @Rule
    public KafkaDriverStartResetRule startResetRule = new KafkaDriverStartResetRule(remoteKafkaDriver);

    @BeforeClass
    public static void createTopic() throws Exception {
        remoteKafkaDriver.createTopic(TOPIC_NAME);
    }


    @Override
    protected KafkaDriver kafkaDriver() {
        return remoteKafkaDriver;
    }
}
