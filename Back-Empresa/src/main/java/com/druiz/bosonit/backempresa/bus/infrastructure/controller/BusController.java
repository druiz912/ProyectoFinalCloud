package com.druiz.bosonit.backempresa.bus.infrastructure.controller;

import com.druiz.bosonit.backempresa.bus.application.BusService;
import com.druiz.bosonit.backempresa.bus.infrastructure.controller.dto.BusDisponibleOutputDto;
import com.druiz.bosonit.backempresa.reserva.infrastructure.controller.dto.ReservasByBusOutputDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("api/v0/bus")
public class BusController {

    @Autowired
    BusService busService;

    @GetMapping("/available/{destinationCity}")
    public ResponseEntity<List<BusDisponibleOutputDto>> getAvailableBuses(
            @PathVariable String destinationCity,
            @RequestParam String lowerDate,
            @RequestParam(required = false, defaultValue = "noDate") String upperDate,
            @RequestParam(required = false, defaultValue = "-1") Float lowerHour,
            @RequestParam(required = false, defaultValue = "-1") Float upperHour) {

        HashMap<String, Object> conditions = new HashMap<>();
        conditions.put("city", destinationCity);
        conditions.put("lowerDate", lowerDate);
        conditions.put("upperDate", upperDate);
        conditions.put("lowerHour", lowerHour);
        conditions.put("upperHour", upperHour);

        List<BusDisponibleOutputDto> busAvailabilityOutputDTOS;
        busAvailabilityOutputDTOS = busService.getBusDisponible(conditions);

        return new ResponseEntity<>(busAvailabilityOutputDTOS, HttpStatus.OK);
    }


    @GetMapping("/reserva/{destinationCity}")
    public ResponseEntity<List<ReservasByBusOutputDto>> getReservaByBus(
            @PathVariable String destinationCity,
            @RequestParam String lowerDate,
            @RequestParam(required = false, defaultValue = "noDate") String upperDate,
            @RequestParam(required = false, defaultValue = "-1") Float lowerHour,
            @RequestParam(required = false, defaultValue = "-1") Float upperHour) {

        HashMap<String, Object> conditions = new HashMap<>();
        conditions.put("city", destinationCity);
        conditions.put("lowerDate", lowerDate);
        conditions.put("upperDate", upperDate);
        conditions.put("lowerHour", lowerHour);
        conditions.put("upperHour", upperHour);

        List<ReservasByBusOutputDto> reservasByBusOutputDtos;
        reservasByBusOutputDtos = busService.getReservaByBus(conditions);

        return new ResponseEntity<>(reservasByBusOutputDtos, HttpStatus.OK);
    }
}