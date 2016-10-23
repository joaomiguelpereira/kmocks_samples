package com.bskyb.ovp.kit.samples;

import com.bskyb.ovp.kafka.driver.KafkaDriver;
import com.bskyb.ovp.kafka.driver.KafkaDriverBuilder;
import com.bskyb.ovp.kit.samples.mainappserver.SimpleServer;
import org.junit.*;

public class LocalFunctionalTest extends FunctionalTest {

    //Create new Local Driver listening on port 5001
    private static final KafkaDriver localKafkaDriver = new KafkaDriverBuilder().brokerPort(5001).build();

    @BeforeClass
    public static void startKafkaAndCreateTopic() throws Exception {
        FunctionalTestHelper.startKafkaAndCreateTopic(localKafkaDriver, TOPIC_NAME);
        SimpleServer.main();
    }

    @AfterClass
    public static void shutDownKafka() {
        FunctionalTestHelper.shutDownKafka(localKafkaDriver);
    }


    @Override
    protected KafkaDriver kafkaDriver() {
        return localKafkaDriver;
    }
}
