package com.druiz.bosonit.backempresa.reserva.infrastructure.controller.dto;

import com.druiz.bosonit.backempresa.reserva.domain.Reserva;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;

import static com.druiz.bosonit.backempresa.config.security.Constants.DATEFORMAT;

@Data
@NoArgsConstructor
public class ReservaOutputDto {
    private Integer id;
    private String city;
    private String name;
    private String surname;
    private String phone;
    private String mail;
    private String date;
    private Float hour;
    private String status;

    public ReservaOutputDto(Reserva reserva) {
        if (reserva == null) return;
        setId(reserva.getId());
        setCity(reserva.getDestinationCity());
        setName(reserva.getName());
        setSurname(reserva.getSurname());
        setPhone(reserva.getPhone());
        setMail(reserva.getMail());
        setDate(new SimpleDateFormat(DATEFORMAT).format(reserva.getDate()));
        setHour(reserva.getHour());
        setStatus(reserva.getStatus());
    }
}
