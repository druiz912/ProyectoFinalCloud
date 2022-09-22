package com.druiz.bosonit.backweb.reserva.application;

import com.druiz.bosonit.backweb.bus.application.BusService;
import com.druiz.bosonit.backweb.bus.domain.Bus;
import com.druiz.bosonit.backweb.config.exceptions.NotFoundException;
import com.druiz.bosonit.backweb.config.kafka.Producer;
import com.druiz.bosonit.backweb.reserva.domain.Reserva;
import com.druiz.bosonit.backweb.reserva.infrastructure.controller.dto.ReservaOutputDto;
import com.druiz.bosonit.backweb.reserva.infrastructure.repo.ReservaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.druiz.bosonit.backweb.config.security.Constants.DATEFORMAT;
import static com.druiz.bosonit.backweb.config.security.Constants.ERRORFORMATDATE;

@Service
public class ReservaServiceImpl implements ReservaService {

    @Autowired
    BusService busService;
    @Autowired
    ReservaRepo reservaRepo;
    @Autowired
    EntityManager entityManager;
    @Autowired
    Producer kafkaProducer;

    @Override
    public Reserva postReserva(Reserva reservaCheck, String condition) {
        Bus bus = busService.findByCityAndDateAndHour(reservaCheck.getDestinationCity(), reservaCheck.getDate(), reservaCheck.getHour());
        Reserva reserva;

        if (bus != null) {
            //El bus existe y cojo los asientos ocupados
            int occupiedSeats = bus.getOccupiedSeats();
            //Comprobamos los asientos
            if (occupiedSeats < 40 && bus.isActive()) {
                reservaCheck.setStatus("Accepted");
                reserva = insertAcceptedReserva(reservaCheck, bus, occupiedSeats, condition);
            } else {
                reservaCheck.setStatus("Cancelled");
                reserva = reservaRepo.save(reservaCheck);
            }
        } else { // Sino está creado, creamos el bus y creamos reserva
            reservaCheck.setStatus("Aceptado!");
            reserva = insertAcceptedReservaAndCreateABus(reservaCheck, condition);
        }
        if (!condition.equals("noSend")) kafkaProducer.sendMessage(reserva);
        return reserva;
    }

    @Override
    public List<ReservaOutputDto> getReservaByConditions(HashMap<String, Object> conditions) {
        List<Reserva> reservas = ReservasByConditions(conditions);
        List<ReservaOutputDto> reservaOutputList = new ArrayList<>();
        reservas.forEach(booking -> {
            try {
                reservaOutputList.add(new ReservaOutputDto(booking));
            } catch (ParseException e) {
                throw new NotFoundException("Parse error");
            }
        });
        return reservaOutputList;
    }

    @Override
    public Reserva findById(Integer id) {
        return reservaRepo.findById(id).orElseThrow(
                () -> new NotFoundException("Id Reserva not found " + id));
    }

    @Override
    public List<ReservaOutputDto> getAllReservas() throws ParseException {
        List<ReservaOutputDto> listReservaOutputDto = new ArrayList<>();
        for(Reserva reserva : reservaRepo.findAll()) listReservaOutputDto.add(new ReservaOutputDto(reserva));
        return listReservaOutputDto;
    }

    // FUNCIONES

    private Reserva insertAcceptedReservaAndCreateABus(Reserva reservaCheck, String condition) {
        Set<Reserva> reservas = new HashSet<>();
        reservas.add(reservaCheck);
        Reserva reservaSave = reservaRepo.save(reservaCheck);
        // Here I create the bus (save)
        Bus newBus = new Bus(null, true,
                reservaCheck.getDestinationCity(), reservaCheck.getHour(), reservaCheck.getDate(), reservas);
        newBus.setOccupiedSeats(1);
        busService.updateBus(newBus);
        return reservaSave;
    }

    private Reserva insertAcceptedReserva(Reserva reservaCheck, Bus bus, int occupiedSeats, String condition) {
        Reserva reserva;
        bus.setOccupiedSeats(occupiedSeats + 1);
        bus.getListReserva().add(reservaCheck);
        //Seteamos el status a Accepted
        reservaCheck.setStatus("Accepted");
        reserva = reservaRepo.save(reservaCheck);
        busService.updateBus(bus);

        return reserva;
    }

    public List<Reserva> ReservasByConditions(Map<String, Object> conditions) {
        // Instancia de CriteriaBuilder llamando al método getCriteriaBuilder()
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        // Instancia de CriteriaQuery<> llamando al método createQuery() --> Llamando a la clase
        CriteriaQuery<Reserva> query = cb.createQuery(Reserva.class);
        // Plantilla --> Reserva.class
        Root<Reserva> root = query.from(Reserva.class);
        List<Predicate> predicates = new ArrayList<>();
        conditions.forEach((field, value) ->
        {
            switch (field) {
                case "city":
                    if (!value.equals("noCity")) {
                        predicates.add(cb.like(root.get("destinationCity"), (String) value));
                    }
                    break;
                case "lowerDate": //en el caso de fecha x debajo
                    Date lowerDate; //definimos
                    try {// pasmaos a formato que tenemos en bbddd
                        lowerDate = new SimpleDateFormat(DATEFORMAT).parse((String) value);
                    } catch (ParseException e) { //sino ha sido posible salta error
                        throw new NotFoundException(ERRORFORMATDATE);
                    }
                    // query de la fecha x arriba
                    predicates.add(cb.greaterThan(root.get("date"), lowerDate));
                    break;
                case "upperDate":
                    if (!value.equals("noDate")) {
                        Date upperDate;
                        try {
                            upperDate = new SimpleDateFormat(DATEFORMAT).parse((String) value);
                        } catch (ParseException e) {
                            throw new NotFoundException(ERRORFORMATDATE);
                        }
                        // query de la fecha x debajo
                        predicates.add(cb.lessThan(root.get("date"), upperDate));
                    }
                    break;
                case "lowerHour":
                    if ((Float) value != -1) {
                        predicates.add(cb.greaterThan(root.get("hour"), (Float) value));
                    }
                    break;
                case "upperHour":
                    if ((Float) value != -1) {
                        predicates.add(cb.lessThan(root.get("hour"), (Float) value));
                    }
                    break;
                case "email":
                    predicates.add(cb.like(root.get("email"), (String) value));
                    break;
                case "equalDate":
                    Date equalDate;
                    try {
                        equalDate = new SimpleDateFormat(DATEFORMAT).parse((String) value);
                    } catch (ParseException e) {
                        throw new NotFoundException(ERRORFORMATDATE);
                    }
                    // Query de la fecha en el caso que sea la misma
                    predicates.add(cb.equal(root.get("date"), equalDate));
                    break;
                case "equalHour":
                    // Query de la hora en el caso que sea la misma
                    predicates.add(cb.le(root.<Float>get("hour"), (Float) value));
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + field);
            }
        });
        query.select(root).where(predicates.toArray(new Predicate[0]));
        List<Reserva> reservas = entityManager.createQuery(query).getResultList();

        if (reservas.isEmpty()) throw new NotFoundException("Reservas está vacio");

        return reservas;
    }
}




