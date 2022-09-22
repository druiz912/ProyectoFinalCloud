package com.druiz.bosonit.backweb;

import com.druiz.bosonit.backweb.reserva.application.ReservaService;
import com.druiz.bosonit.backweb.reserva.domain.Reserva;
import com.druiz.bosonit.backweb.reserva.infrastructure.controller.dto.ReservaInputDto;
import com.druiz.bosonit.backweb.reserva.infrastructure.repo.ReservaRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
public class ReservaServiceTest {

        @Autowired
        ReservaService service;

        @Autowired
        ReservaRepo repo;

        @Test
        @Order(1)
        @Rollback(value = false)
        void testPostReserva() {
            ReservaInputDto reservaInputDto = new ReservaInputDto();
            reservaInputDto.setName("TEST");
            reservaInputDto.setSurname("TEST");
            reservaInputDto.setMail("test@test.com");
            reservaInputDto.setCity("Barcelona");
            reservaInputDto.setHour(8F);
            Reserva reserva = new Reserva(reservaInputDto);
            service.postReserva(reserva, "send");
            Assertions.assertThat(reserva.getId()).isPositive();
        }

        @Test
        @Order(2)
        void testGetReservaById() {
            Reserva reserva = service.findById(1);
            System.out.println("id: " + reserva.getId());
            Assertions.assertThat(reserva.getId()).isEqualTo(1);
        }

        @Test
        @Order(3)
        void testGetList() {
            List<Reserva> reserva = repo.findAll();
            Assertions.assertThat(reserva.size()).isNotZero();
        }


}

