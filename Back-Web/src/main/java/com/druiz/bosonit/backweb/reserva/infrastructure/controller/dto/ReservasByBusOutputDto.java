package com.druiz.bosonit.backweb.reserva.infrastructure.controller.dto;

import com.druiz.bosonit.backweb.bus.domain.Bus;
import com.druiz.bosonit.backweb.reserva.domain.Reserva;
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
