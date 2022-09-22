package com.druiz.bosonit.backweb.reserva.infrastructure.controller;

import com.druiz.bosonit.backweb.config.exceptions.BadRequest;
import com.druiz.bosonit.backweb.config.exceptions.UnprocesableException;
import com.druiz.bosonit.backweb.reserva.application.ReservaService;
import com.druiz.bosonit.backweb.reserva.domain.Reserva;
import com.druiz.bosonit.backweb.reserva.infrastructure.controller.dto.ReservaInputDto;
import com.druiz.bosonit.backweb.reserva.infrastructure.controller.dto.ReservaOutputDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

import static com.druiz.bosonit.backweb.config.security.Constants.*;

@RestController
@RequestMapping("api/v0/reservas")
public class ReservaController {

    @Autowired
    ReservaService reservaService;

    @PostMapping
    public ResponseEntity<?> postReserva(@Valid @RequestBody ReservaInputDto reservaInputDto) throws ParseException {
        Reserva reservaConvert;
        Reserva reservaBack;
        if(!CITIES.contains(reservaInputDto.getCity())) throw new UnprocesableException(ERRORCITIES);
        if(!HOURS.contains(reservaInputDto.getHour())) throw new UnprocesableException(ERRORHOURS);
        try {
            reservaConvert = new Reserva(reservaInputDto);
            reservaBack = reservaService.postReserva(reservaConvert, "send");
        } catch (Exception e) {
            return new ResponseEntity<>(new BadRequest("Formato de la reserva no valido"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ReservaOutputDto(reservaBack), HttpStatus.OK);
    }

    @GetMapping("/{city}")
    public ResponseEntity<?> getReservaByConditions(
            @PathVariable String city,
            @RequestParam String lowerDate,
            @RequestParam(required = false, defaultValue = "noDate") String upperDate,
            @RequestParam(required = false, defaultValue = "-1") Float lowerHour,
            @RequestParam(required = false, defaultValue = "-1") Float upperHour) {
        HashMap<String, Object> conditions = new HashMap<>();
        conditions.put("city", city);
        conditions.put("lowerDate", lowerDate);
        conditions.put("upperDate", upperDate);
        conditions.put("lowerHour", lowerHour);
        conditions.put("upperHour", upperHour);

        //Feign a Empresa para saber las reservas reales que hay
        List<ReservaOutputDto> reservaConditions = reservaService.getReservaByConditions(conditions);
        return new ResponseEntity<>(reservaConditions, HttpStatus.OK);
    }

    @GetMapping
    public List<ReservaOutputDto> getAllReservas() throws ParseException {
        return reservaService.getAllReservas();
    }
}
