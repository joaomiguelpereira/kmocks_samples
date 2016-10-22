package com.bskyb.ovp.kit.samples;

import com.bskyb.ovp.kafka.driver.KafkaDriver;

import static org.assertj.core.api.Assertions.assertThat;

public class FunctionalTestHelper {


    protected static void startKafkaAndCreateTopic(KafkaDriver driver, String topicName) throws Exception {
        driver.startup();
        assertThat(driver.isKafkaRunning());
        driver.createTopic(topicName);
        assertThat(driver.listTopics()).contains(topicName);
    }

    protected static void shutDownKafka(KafkaDriver driver) {
        driver.shutdown();
        assertThat(!driver.isKafkaRunning());
    }


}
