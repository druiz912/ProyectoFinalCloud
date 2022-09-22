package com.druiz.bosonit.backempresa.config.kafka;

import com.druiz.bosonit.backempresa.reserva.application.ReservaService;
import com.druiz.bosonit.backempresa.reserva.domain.Reserva;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Listener {
        @Autowired
        ReservaService reservaService;

        @KafkaListener(topics = "reservas", groupId = "myGroup")
        public void consumeMessage(String reserva) {
            log.info("*****RESERVA RECIBIDA DESDE BACKWEB*****" );
            Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy HH:mm:ss").create();
            Reserva reservaByBackWeb = gson.fromJson(reserva, Reserva.class);
            reservaByBackWeb.setEmailSet(null);
            reservaService.postReserva(reservaByBackWeb, "listen");
            log.info("MENSAJE RECIBIDO: {}", reserva);
        }
    }

