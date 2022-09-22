package com.druiz.bosonit.backempresa.bus.infrastructure.controller.dto;

import com.druiz.bosonit.backempresa.bus.domain.Bus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;

import static com.druiz.bosonit.backempresa.config.security.Constants.*;

@Data
@NoArgsConstructor
public class BusDisponibleOutputDto {

    private String city;
    private String exitDate;
    private Float exitHour;
    private Integer availableSeats;

    public BusDisponibleOutputDto(Bus bus) {
        setCity(bus.getCity());
        setExitDate(new SimpleDateFormat(DATEFORMAT).format(bus.getDate()));
        setExitHour(bus.getHour());
        setAvailableSeats(DEFAULTCAPACITY - bus.getOccupiedSeats());
    }
}