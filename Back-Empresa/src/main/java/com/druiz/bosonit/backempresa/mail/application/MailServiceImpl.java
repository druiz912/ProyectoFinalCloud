package com.druiz.bosonit.backempresa.mail.application;

import com.druiz.bosonit.backempresa.mail.domain.Mail;
import com.druiz.bosonit.backempresa.mail.infrastructure.controller.dto.MailInputDto;
import com.druiz.bosonit.backempresa.mail.infrastructure.controller.dto.MailOutputDto;
import com.druiz.bosonit.backempresa.mail.infrastructure.repo.MailRepo;
import com.druiz.bosonit.backempresa.reserva.application.ReservaAux;
import com.druiz.bosonit.backempresa.reserva.domain.Reserva;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.druiz.bosonit.backempresa.config.security.Constants.*;

@Service
@Slf4j
public class MailServiceImpl implements MailService {
    @Autowired
    MailRepo mailRepo;

    @Autowired
    ReservaAux reservaService;

    @Override
    public void postMail(Mail mailReceived) {
        mailRepo.save(mailReceived);
    }

    @Override
    public List<MailOutputDto> getMailsByConditions(HashMap<String, Object> conditions) {
        List<Reserva> reservas = reservaService.ReservasByConditions(conditions);
        List<MailOutputDto> mailList = new ArrayList<>();

        reservas.forEach(reserva -> reserva.getEmailSet().forEach(mail -> mailList.add(new MailOutputDto(mail, reserva))));
        return mailList;
    }

    @Override
    public MailOutputDto resend(MailInputDto mail) {
        HashMap<String, Object> conditions = new HashMap<>();
        conditions.put("city", mail.getCity());
        conditions.put("equalDate", mail.getDate());
        conditions.put("equalHour", mail.getHour());
        conditions.put("mail", mail.getMail());

        List<Reserva> reservas = reservaService.ReservasByConditions(conditions);
        Reserva reservaFiltrada = reservas.get(0);
        // Envío de email
        SendMail.sendMail(
                reservaFiltrada.getMail(),
                VIRTUALTRAVELMAIL,
                "Reenvío de reserva",
                reSendMessage(reservaFiltrada),
                "html"
        );
        Mail newMail = new Mail(null, VIRTUALTRAVELMAIL,
                reservaFiltrada.getMail(),
                "Reenvío de reserva");
        postMail(newMail);

        Set<Mail> mailSet = reservaFiltrada.getEmailSet();
        mailSet.add(newMail);
        reservaFiltrada.setEmailSet(mailSet);
        // Se vuelve a guardar y si ya está da error de que el usuario ya está registrado
        Reserva reservaSaved = reservaService.saveReserva(reservaFiltrada);
        return new MailOutputDto(newMail, reservaSaved);

    }


    private String reSendMessage(Reserva reserva) {
        if (reserva.getStatus().equals("Accepted")) {
            return "<h1>Reserva aceptada<h1>" +
                    "<h3>Destino: " + reserva.getDestinationCity() + "</h3>" +
                    "<h3>Fecha: " + formatDate(reserva.getDate()) + "</h3>" +
                    "<h3>Hora: " + reserva.getHour() + "</h3>" +
                    "<h3>Identificador del viaje: " + reserva.getId() + "</h3>";
        } else {
            return "<h1>Reserva aceptada<h1>" +
                    "<h3>Lo sentimos pero tu reserva con destino: " + reserva.getDestinationCity() +
                    "con fecha: " + formatDate(reserva.getDate()) + " y hora: " + reserva.getHour() +
                    " ha sido cancelado </h3>";
        }
    }
    //Formatear Date
    private String formatDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(DATEFORMAT);
        return dateFormat.format(date);
    }
}
