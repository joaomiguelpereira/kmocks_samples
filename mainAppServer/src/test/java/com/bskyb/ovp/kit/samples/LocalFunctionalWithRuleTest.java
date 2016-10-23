package com.bskyb.ovp.kit.samples;

import com.bskyb.ovp.kafka.driver.KafkaDriver;
import com.bskyb.ovp.kafka.driver.KafkaDriverBuilder;
import com.bskyb.ovp.kafka.driver.KafkaDriverStartResetRule;
import com.bskyb.ovp.kafka.driver.KafkaDriverStartStopRule;
import com.bskyb.ovp.kit.samples.mainappserver.SimpleServer;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;

public class LocalFunctionalWithRuleTest extends FunctionalTest{

    //Create new Local Driver listening on port 5001
    private static final KafkaDriver localKafkaDriver = new KafkaDriverBuilder().brokerPort(5001).build();

    @ClassRule
    public static KafkaDriverStartStopRule startStopRule = new KafkaDriverStartStopRule(localKafkaDriver);

    @Rule
    public KafkaDriverStartResetRule startResetRule = new KafkaDriverStartResetRule(localKafkaDriver);

    @BeforeClass
    public static void createTopic() throws Exception {
        SimpleServer.main();
        localKafkaDriver.createTopic(TOPIC_NAME);
    }


    @Override
    protected KafkaDriver kafkaDriver() {
        return localKafkaDriver;
    }
}
