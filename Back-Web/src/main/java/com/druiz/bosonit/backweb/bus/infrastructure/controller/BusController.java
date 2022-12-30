package com.druiz.bosonit.backweb.bus.infrastructure.controller;

import com.druiz.bosonit.backweb.bus.application.BusService;
import com.druiz.bosonit.backweb.bus.infrastructure.controller.dto.output.BusDisponibleOutputDto;
import com.druiz.bosonit.backweb.reserva.application.ReservaFeign;
import com.druiz.bosonit.backweb.reserva.infrastructure.controller.dto.ReservasByBusOutputDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/v0/bus")
public class BusController {

    @Autowired
    BusService busService;

    @Autowired
    ReservaFeign reservaFeign;

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

        List<BusDisponibleOutputDto> busDisponibleOutputDto;
        busDisponibleOutputDto = busService.getBusDisponible(conditions);

        return new ResponseEntity<>(busDisponibleOutputDto, HttpStatus.OK);
    }

    // 2. Plazas libres. Ruta: GET /disponible/:ciudadDestino
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
        reservasByBusOutputDtos = reservaFeign.getReservaByBus(conditions);

        return new ResponseEntity<>(reservasByBusOutputDtos, HttpStatus.OK);
    }


}
