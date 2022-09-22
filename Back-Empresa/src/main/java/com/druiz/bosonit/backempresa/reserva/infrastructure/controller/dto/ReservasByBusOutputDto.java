package com.druiz.bosonit.backempresa.reserva.infrastructure.controller.dto;

import com.druiz.bosonit.backempresa.bus.domain.Bus;
import com.druiz.bosonit.backempresa.reserva.domain.Reserva;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@NoArgsConstructor
public class ReservasByBusOutputDto {
    @NotNull
    private Set<Reserva> reservaSet;

    public ReservasByBusOutputDto(Bus bus) {
        setReservaSet(bus.getListReserva());
    }
}
