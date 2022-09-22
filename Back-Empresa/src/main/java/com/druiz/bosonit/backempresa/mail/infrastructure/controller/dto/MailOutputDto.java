package com.druiz.bosonit.backempresa.mail.infrastructure.controller.dto;

import com.druiz.bosonit.backempresa.mail.domain.Mail;
import com.druiz.bosonit.backempresa.reserva.domain.Reserva;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;

import static com.druiz.bosonit.backempresa.config.security.Constants.DATEFORMAT;

@Data
@NoArgsConstructor
public class MailOutputDto {
    private String destinationCity;
    private String receiver;
    private String reservaDate;
    private Float reservaHour;
    private String subject;

    public MailOutputDto(Mail mail, Reserva reserva) {
        this.destinationCity = reserva.getDestinationCity();
        this.receiver = mail.getReceiver();
        this.reservaDate = new SimpleDateFormat(DATEFORMAT).format(reserva.getDate());
        this.reservaHour = reserva.getHour();
        this.subject = mail.getSubject();
    }

}
