package com.druiz.bosonit.backweb.bus.infrastructure.controller.dto.input;

import com.druiz.bosonit.backweb.bus.domain.Bus;
import com.druiz.bosonit.backweb.reserva.domain.Reserva;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
public class BusInputDto {

    @NotNull
    private Integer id;

    @NotNull(message = "Active can't be null")
    private boolean active;

    @NotBlank(message = "City can't be null")
    private String city;

    @NotBlank(message = "Seats can't be empty")
    private int occupiedSeats;

    @NotBlank(message = "Hour can't be empty")
    private Float hour;

    @NotBlank(message = "Date can't be empty")
    private Date date;

    private Set<Reserva> reservaSet;

    public Bus convertToEntity() {
        return new Bus(null, active, city, hour, date, reservaSet);
    }
}
