package com.druiz.bosonit.backempresa.reserva.application;

import com.druiz.bosonit.backempresa.config.exceptions.NotFoundException;
import com.druiz.bosonit.backempresa.config.exceptions.UnprocesableException;
import com.druiz.bosonit.backempresa.reserva.domain.Reserva;
import com.druiz.bosonit.backempresa.reserva.infrastructure.ReservaRepo;
import com.druiz.bosonit.backempresa.reserva.infrastructure.controller.dto.ReservaOutputDto;
import com.druiz.bosonit.backempresa.user.application.UserService;
import com.druiz.bosonit.backempresa.user.domain.User;
import com.druiz.bosonit.backempresa.user.infrastructure.controller.dto.UserInputDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.druiz.bosonit.backempresa.config.security.Constants.DATEFORMAT;

@Service
public class ReservaAux {

    @Autowired
    ReservaRepo reservaRepo;
    @Autowired
    EntityManager entityManager;
    @Autowired
    UserService userService;

    public Reserva saveReserva(Reserva reserva) {
        User wantUser = userService.findUserByMailAndByPassword(reserva.getMail(), reserva.getPhone());
        if (wantUser != null) {
            throw new UnprocesableException("El usuario: " + wantUser.getMail() + " " + wantUser.getId() + " " + "ya existe");
        } else {
            UserInputDto userInputDto = new UserInputDto(reserva.getMail(), reserva.getPhone(), "ROLE_USER");
            User user = new User(userInputDto);
            userService.saveUser(user);
            return reservaRepo.save(reserva);
        }
    }

    public List<ReservaOutputDto> getAllReservas() throws ParseException {
        List<ReservaOutputDto> listReservaOutputDto = new ArrayList<>();
        for(Reserva reserva : reservaRepo.findAll()) listReservaOutputDto.add(new ReservaOutputDto(reserva));
        return listReservaOutputDto;
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
                    Date lowerDate;
                    try {// pasmaos a formato que tenemos en bbddd
                        lowerDate = new SimpleDateFormat(DATEFORMAT).parse((String) value);
                    } catch (ParseException e) { //sino ha sido posible salta error
                        throw new NotFoundException("No es válido el formato de la fecha");
                    }
                    // query
                    predicates.add(cb.greaterThan(root.get("date"), lowerDate));
                    break;
                case "upperDate":
                    if (!value.equals("noDate")) {
                        Date upperDate;
                        try {
                            upperDate = new SimpleDateFormat(DATEFORMAT).parse((String) value);
                        } catch (ParseException e) {
                            throw new NotFoundException("Invalid date format");
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
                case "mail":
                    predicates.add(cb.like(root.get("mail"), (String) value));
                    break;
                case "equalDate":
                    Date equalDate;
                    try {
                        equalDate = new SimpleDateFormat(DATEFORMAT).parse((String) value);
                    } catch (ParseException e) {
                        throw new NotFoundException("Invalid date format");
                    }
                    predicates.add(cb.equal(root.get("date"), equalDate));
                    break;
                case "equalHour":
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

    public Reserva findById(Integer id) {
        return reservaRepo.findById(id).orElseThrow(
                () -> new NotFoundException("Id Reserva not found " + id));
    }


}
