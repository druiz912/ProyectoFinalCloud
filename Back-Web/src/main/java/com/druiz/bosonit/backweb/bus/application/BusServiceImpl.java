package com.druiz.bosonit.backweb.bus.application;

import com.druiz.bosonit.backweb.bus.domain.Bus;
import com.druiz.bosonit.backweb.bus.infrastructure.controller.dto.output.BusDisponibleOutputDto;
import com.druiz.bosonit.backweb.bus.infrastructure.repo.BusRepo;
import com.druiz.bosonit.backweb.config.exceptions.NotFoundException;
import com.druiz.bosonit.backweb.reserva.infrastructure.controller.dto.ReservasByBusOutputDto;
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
public class BusServiceImpl implements BusService {


    @Autowired
    BusRepo busRepo;

    @Autowired
    EntityManager entityManager;


    @Override
    public void updateBus(Bus bus) {
        busRepo.saveAndFlush(bus);
    }

    @Override
    public List<BusDisponibleOutputDto> getBusDisponible(HashMap<String, Object> conditions) {
        List<Bus> busesReceived = AvailableBuses(conditions);

        List<BusDisponibleOutputDto> busOutputDto = new ArrayList<>();
        busesReceived.forEach(bus -> busOutputDto.add(new BusDisponibleOutputDto(bus)));

        return busOutputDto;
    }

    @Override
    public List<ReservasByBusOutputDto> getReservaByBus(HashMap<String, Object> conditions) {
        List<Bus> busesReceived;
        busesReceived = ReservasByBus(conditions);

        List<ReservasByBusOutputDto> reservasByBusOutputDtos = new ArrayList<>();
        busesReceived.forEach(bus -> reservasByBusOutputDtos.add(new ReservasByBusOutputDto(bus)));

        return reservasByBusOutputDtos;
    }

    @Override
    public Bus findByCityAndDateAndHour(String city, Date date, Float hour) {
        return busRepo.findByCityAndDateAndHour(city, date, hour);
    }

    // FUNCIONES

    public List<Bus> AvailableBuses(Map<String, Object> conditions) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Bus> query = cb.createQuery(Bus.class);
        Root<Bus> root = query.from(Bus.class);
        List<Predicate> predicates = new ArrayList<>();

        conditions.forEach((field, value) ->
        {
            switch (field) {
                case "city":
                    predicates.add(cb.like(root.get("city"), (String) value));
                    break;
                case "lowerDate":
                    Date lowerDate;
                    try {
                        lowerDate = new SimpleDateFormat(DATEFORMAT).parse((String) value);
                    } catch (ParseException e) {
                        throw new NotFoundException(ERRORFORMATDATE + value);
                    }
                    predicates.add(cb.greaterThan(root.get("date"), lowerDate));
                    break;
                case "upperDate":
                    if (!value.equals("noDate")) {
                        Date upperDate;
                        try {
                            upperDate = new SimpleDateFormat(DATEFORMAT).parse((String) value);
                        } catch (ParseException e) {
                            throw new NotFoundException(ERRORFORMATDATE + value);
                        }
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
                case "equalDate":
                    Date equalDate;
                    try {
                        equalDate = new SimpleDateFormat(DATEFORMAT).parse((String) value);
                    } catch (ParseException e) {
                        throw new NotFoundException(ERRORFORMATDATE + value);
                    }
                    predicates.add(cb.equal(root.get("date"), equalDate));
                    break;
                case "equalHour":
                    predicates.add(cb.le(root.<Float>get("hour"), (Float) value));
                    break;
                default:
                    throw new IllegalArgumentException("predicates not supported");
            }
        });
        // El numero de asientos es menor que 41?
        predicates.add(cb.lessThan(root.get("occupiedSeats"), 41));
        query.select(root).where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(query).getResultList();
    }

    public List<Bus> ReservasByBus(Map<String, Object> conditions) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Bus> query = cb.createQuery(Bus.class);
        Root<Bus> root = query.from(Bus.class);
        List<Predicate> predicates = new ArrayList<>();

        conditions.forEach((field, value) ->
        {
            switch (field) {
                case "city":
                    predicates.add(cb.like(root.get("city"), (String) value));
                    break;
                case "lowerDate":
                    Date lowerDate;
                    try {
                        lowerDate = new SimpleDateFormat(DATEFORMAT).parse((String) value);
                    } catch (ParseException e) {
                        throw new NotFoundException(ERRORFORMATDATE + value);
                    }
                    predicates.add(cb.greaterThan(root.get("date"), lowerDate));
                    break;
                case "upperDate":
                    if (!value.equals("noDate")) {
                        Date upperDate;
                        try {
                            upperDate = new SimpleDateFormat(DATEFORMAT).parse((String) value);
                        } catch (ParseException e) {
                            throw new NotFoundException(ERRORFORMATDATE + value);
                        }
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
                default:
                    throw new IllegalArgumentException("Unknown predicates");
            }
        });
        query.select(root).where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(query).getResultList();
    }


}
