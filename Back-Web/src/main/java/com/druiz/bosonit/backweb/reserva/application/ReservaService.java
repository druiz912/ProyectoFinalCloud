package com.druiz.bosonit.backweb.reserva.application;

import com.druiz.bosonit.backweb.reserva.domain.Reserva;
import com.druiz.bosonit.backweb.reserva.infrastructure.controller.dto.ReservaOutputDto;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

public interface ReservaService {
    Reserva postReserva(Reserva reservaCheck, String condition);
    List<ReservaOutputDto> getReservaByConditions(HashMap<String, Object> conditions);
    Reserva findById(Integer id);
    List<ReservaOutputDto> getAllReservas() throws ParseException;
}
