package com.druiz.bosonit.backempresa.bus.infrastructure.controller.dto;

import com.druiz.bosonit.backempresa.bus.domain.Bus;
import com.druiz.bosonit.backempresa.reserva.domain.Reserva;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
public class BusInputDto {

    private Integer id;

    @NotNull(message = "Active can't be null")
    private boolean active;

    @NotEmpty(message = "City can't be empty")
    private String city;

    @NotEmpty(message = "seats can't be empty")
    private int occupiedSeats;

    @NotEmpty(message = "Hour can't be empty")
    private Float hour;

    @NotEmpty(message = "Date can't be empty")
    private Date date;

    private Set<Reserva> reservaSet;

    public Bus convertToEntity() {
        return new Bus(null, active, city, hour, date, reservaSet);
    }
}
