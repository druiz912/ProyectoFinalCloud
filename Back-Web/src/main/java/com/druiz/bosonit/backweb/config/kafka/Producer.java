package com.druiz.bosonit.backweb.config.kafka;

import com.druiz.bosonit.backweb.reserva.domain.Reserva;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class Producer {

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(Reserva reserva) {
        Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy HH:mm:ss").create();
        String JsonReserva = gson.toJson(reserva);
        Message<String> message = MessageBuilder
                .withPayload(JsonReserva)
                .setHeader(KafkaHeaders.TOPIC, "reservas")
                .build();
        kafkaTemplate.send(message);
        log.info("****MENSAJE ENVIADO**** %s" + reserva);

    }
}
