package com.druiz.bosonit.backweb.bus.application;

import com.druiz.bosonit.backweb.bus.domain.Bus;
import com.druiz.bosonit.backweb.bus.infrastructure.controller.dto.output.BusDisponibleOutputDto;
import com.druiz.bosonit.backweb.reserva.infrastructure.controller.dto.ReservasByBusOutputDto;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface BusService {
    List<BusDisponibleOutputDto> getBusDisponible(HashMap<String, Object> conditions);
    void updateBus(Bus bus);
    List<ReservasByBusOutputDto> getReservaByBus(HashMap<String, Object> conditions);

    Bus findByCityAndDateAndHour(String city, Date date, Float hour);
}
