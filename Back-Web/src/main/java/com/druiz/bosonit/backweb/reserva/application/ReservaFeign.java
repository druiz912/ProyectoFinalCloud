package com.druiz.bosonit.backweb.reserva.application;

import com.druiz.bosonit.backweb.reserva.infrastructure.controller.dto.ReservasByBusOutputDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@FeignClient(name = "BackEmpresa")
public interface ReservaFeign {

    @GetMapping("backempresa:8081/api/v0/bus/reserva/{destinationCity}")
    List<ReservasByBusOutputDto> getReservaByBus(Map<String, Object> conditions);



}
