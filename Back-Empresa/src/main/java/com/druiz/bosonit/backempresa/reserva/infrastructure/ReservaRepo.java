package com.druiz.bosonit.backempresa.reserva.infrastructure;

import com.druiz.bosonit.backempresa.reserva.domain.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepo extends JpaRepository<Reserva,Integer> {
    Reserva findByMail(String mail);
}

