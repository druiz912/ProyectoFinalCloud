package com.druiz.bosonit.backweb.reserva.infrastructure.repo;

import com.druiz.bosonit.backweb.reserva.domain.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepo extends JpaRepository<Reserva,Integer> {
    // Es buena practica devolver la entidad en sí,
    // ya que si ponemos un DTO el DTO puede variar sus datos o no...Y la entidad tenemos que procurar que no...
}
