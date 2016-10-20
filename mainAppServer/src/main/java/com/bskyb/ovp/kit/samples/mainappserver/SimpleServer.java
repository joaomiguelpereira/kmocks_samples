package com.bskyb.ovp.kit.samples.mainappserver;


import com.bskyb.ovp.kit.sample.mainapp.MainApplication;
import com.google.common.collect.Iterables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Configuration
@RestController
@SpringBootApplication
public class SimpleServer {


    @Value("${kafka.boostrapUri}")
    private String brokerUri;

    @Value("${kafka.topic}")
    private String topic;

    @Autowired
    private MainApplication mainApplication;

    public static void main(String...args) throws Exception {
        SpringApplication.run(SimpleServer.class, args);
    }

    @RequestMapping(value = "/messages", method = RequestMethod.POST)
    public void addMessages(@RequestBody List<String> messages) {
        mainApplication.sendSomeMessages(Iterables.toArray(messages, String.class));
    }

    @Bean
    public MainApplication mainApplication() {
        return new MainApplication(brokerUri, topic);
    }


}
