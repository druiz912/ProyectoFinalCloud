package com.druiz.bosonit.backweb.config.kafka;

import com.druiz.bosonit.backweb.reserva.application.ReservaService;
import com.druiz.bosonit.backweb.reserva.domain.Reserva;
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

    @KafkaListener(topics = "reservas-disponibles", groupId = "myGroup")
    public void consumeMessage(String reserva) {
        log.info("*****RESERVA RECIBIDA DESDE BACKEMPRESA*****");
        Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy HH:mm:ss").create();
        Reserva reservaByBackEmpresa = gson.fromJson(reserva, Reserva.class);
        try{
            reservaService.postReserva(reservaByBackEmpresa, "noSend");
            log.info("MENSAJE RECIBIDO: {}", reserva);
        } catch(Exception e) {
            throw new IllegalStateException("NO SE PUDO GUARDAR LA RESERVA");
        }
    }
}
