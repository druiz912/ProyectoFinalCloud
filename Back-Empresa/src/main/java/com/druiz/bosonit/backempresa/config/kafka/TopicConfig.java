package com.druiz.bosonit.backempresa.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class TopicConfig {

    public NewTopic newTopic() {
        return TopicBuilder.name("reservas-disponibles").build();
    }

}