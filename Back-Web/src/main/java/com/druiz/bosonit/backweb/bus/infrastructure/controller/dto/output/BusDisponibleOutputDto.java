package com.druiz.bosonit.backweb.bus.infrastructure.controller.dto.output;

import com.druiz.bosonit.backweb.bus.domain.Bus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;

import static com.druiz.bosonit.backweb.config.security.Constants.DATEFORMAT;

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
        setAvailableSeats(40 - bus.getOccupiedSeats());
    }
}