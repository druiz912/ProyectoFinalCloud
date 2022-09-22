package com.druiz.bosonit.backempresa.reserva.application;

import com.druiz.bosonit.backempresa.bus.application.BusService;
import com.druiz.bosonit.backempresa.bus.domain.Bus;
import com.druiz.bosonit.backempresa.config.kafka.Producer;
import com.druiz.bosonit.backempresa.mail.application.MailService;
import com.druiz.bosonit.backempresa.mail.application.SendMail;
import com.druiz.bosonit.backempresa.mail.domain.Mail;
import com.druiz.bosonit.backempresa.reserva.domain.Reserva;
import com.druiz.bosonit.backempresa.reserva.infrastructure.controller.dto.ReservaOutputDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.druiz.bosonit.backempresa.config.security.Constants.DATEFORMAT;
import static com.druiz.bosonit.backempresa.config.security.Constants.VIRTUALTRAVELMAIL;


@Service
public class ReservaServiceImpl implements ReservaService {
    @Autowired
    BusService busService;
    @Autowired
    MailService mailService;
    @Autowired
    ReservaAux reservaAux;
    @Autowired
    Producer kafkaProducer;


    @Override
    public Reserva postReserva(Reserva reservaCheck, String condition) {
        Bus bus = busService.findByCityAndDateAndHour(reservaCheck.getDestinationCity(), reservaCheck.getDate(), reservaCheck.getHour());
        Reserva reserva = null;

        if (bus != null) {
            // El bus ya existe y cojo sus asientos
            int occupiedSeats = bus.getOccupiedSeats();
            if (occupiedSeats < 40 && bus.isActive()) {
                reservaCheck.setStatus("Accepted");
                reserva = insertAcceptedReserva(reservaCheck, bus, occupiedSeats, condition);
                //Confirmación de email
                SendMail.sendMail(
                        reserva.getMail(),
                        VIRTUALTRAVELMAIL,
                        "Confirmación de reserva",
                        confirmationMessage(reserva),
                        "html"
                );
            } else {
                if (occupiedSeats >= 40) {
                    reservaCheck.setStatus("Cancelled");
                    reserva = createMailAndReserva(reservaCheck, "Reserva cancelada (Bus lleno)", condition);
                    // Email de cancelación de viaje por bus lleno
                    SendMail.sendMail(
                            reserva.getMail(),
                            VIRTUALTRAVELMAIL,
                            "Reserva cancelada, el bus va lleno",
                            reservaCancelled("full", reserva),
                            "html"
                    );
                } else if (!bus.isActive()) {
                    reserva = createMailAndReserva(reservaCheck, "Reserva cancelada", condition);
                    // ENVIO DE EMAIL DE BUS INACTIVO POR HUELGA
                    SendMail.sendMail(
                            reserva.getMail(),
                            VIRTUALTRAVELMAIL,
                            "Reserva cancelada",
                            reservaCancelled("inactive", reserva),
                            "html"
                    );
                }
            }
        } else { // If there is no bus created in BD...
            reservaCheck.setStatus("Accepted!");
            reserva = insertAcceptedReservaAndCreateABus(reservaCheck, condition);
            //Sending confirmation email
            SendMail.sendMail(
                    reserva.getMail(),
                    VIRTUALTRAVELMAIL,
                    "Confirmación de reserva",
                    confirmationMessage(reserva),
                    "html"
            );
        }
        // Si creo la reserva directamente en backempresa (requiere token) envío a backweb
        if (condition.equals("direct")) kafkaProducer.sendMessage(reserva);
        return reserva;
    }


    @Override
    public List<ReservaOutputDto> getReservaByConditions(HashMap<String, Object> conditions) {
        List<Reserva> reservas = reservaAux.ReservasByConditions(conditions);
        List<ReservaOutputDto> reservaOutputList = new ArrayList<>();
        reservas.forEach(reservax -> reservaOutputList.add(new ReservaOutputDto(reservax)));
        return reservaOutputList;
    }

    @Override
    public List<ReservaOutputDto> getAllReservas() throws ParseException {
        return reservaAux.getAllReservas();
    }

    // FUNCIONES

    private Reserva createMailAndReserva(Reserva reservaCheck, String subject, String condition) {
        Reserva reservaSaved;
        //Creamos mail
        Mail newMail = new Mail(null, VIRTUALTRAVELMAIL, reservaCheck.getMail(), subject);
        mailService.postMail(newMail);
        //
        Set<Mail> mailSet = new HashSet<>();
        mailSet.add(newMail);
        reservaCheck.setEmailSet(mailSet);
        // Usamos la funcion postReserva y le pasamos por parametro la reserva despues de todos los filtros
        reservaSaved = reservaAux.saveReserva(reservaCheck);
        return reservaSaved;
    }

    private Reserva insertAcceptedReservaAndCreateABus(Reserva reservaCheck, String condition) {
        // Instanciando Mail
        Mail newMail = new Mail(null, VIRTUALTRAVELMAIL, reservaCheck.getMail(),
                "Confirmación de reserva");
        mailService.postMail(newMail);
        //
        Set<Mail> mailSet = new HashSet<>();
        mailSet.add(newMail);
        reservaCheck.setEmailSet(mailSet);
        //
        Set<Reserva> reservas = new HashSet<>();
        reservas.add(reservaCheck);
        Reserva reservaSaved = reservaAux.saveReserva(reservaCheck);
        //
        Bus newBus = new Bus(null, true,
                reservaCheck.getDestinationCity(), reservaCheck.getHour(), reservaCheck.getDate(), reservas);
        newBus.setOccupiedSeats(1);
        busService.updateBus(newBus);
        return reservaSaved;
    }

    private Reserva insertAcceptedReserva(Reserva reservaCheck, Bus bus, int occupiedSeats, String condition) {
        Reserva reservaSaved = createMailAndReserva(reservaCheck, "Confirmación de reserva", condition);

        bus.setOccupiedSeats(occupiedSeats + 1);
        bus.getListReserva().add(reservaCheck);
        busService.updateBus(bus);
        return reservaSaved;
    }

    // reset date with a pattern
    private String formatDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(DATEFORMAT);
        return dateFormat.format(date);
    }

    //Mensaje de confirmación correo
    private String confirmationMessage(Reserva reserva) {
        return "<h1>Virtual Travel - Reserva aceptada<h1>" +
                "<h3>Destino: " + reserva.getDestinationCity() + "</h3>" +
                "<h3>Fecha: " + formatDate(reserva.getDate()) + "</h3>" +
                "<h3>Hora: " + reserva.getHour() + "</h3>" +
                "<h3>Identificador del billete: " + reserva.getId() + "</h3>" +
                "<h3>Gracias " + reserva.getName() + " y buen viaje!</h3>";
    }

    //Reserva cancelada
    private String reservaCancelled(String condition, Reserva reserva) {
        return "<h1>Virtual Travel - Reserva Cancelada <h1>" +
                "<h3>Lo sentimos pero su reserva ha sido cancelada: " +
                " Destino: " + reserva.getDestinationCity() +
                "Fecha: " + formatDate(reserva.getDate()) + " a las "
                + reserva.getHour() +
                " ha sido cancelada debido a" +
                (condition.equals("full")
                        ? " el autobus va lleno"
                        : " hay una huelga de conductores")
                + " </h3>";
    }

}