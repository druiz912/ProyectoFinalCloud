package com.druiz.bosonit.backempresa.reserva.application;

import com.druiz.bosonit.backempresa.reserva.domain.Reserva;
import com.druiz.bosonit.backempresa.reserva.infrastructure.controller.dto.ReservaOutputDto;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

public interface ReservaService {
    Reserva postReserva(Reserva reservaCheck, String condition);
    List<ReservaOutputDto> getReservaByConditions(HashMap<String, Object> conditions);

    List<ReservaOutputDto> getAllReservas() throws ParseException;
}