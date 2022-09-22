package com.druiz.bosonit.backweb.bus.infrastructure.repo;

import com.druiz.bosonit.backweb.bus.domain.Bus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;


public interface BusRepo extends JpaRepository<Bus, Integer> {
    // Es buena practica devolver la entidad en sí,
    // ya que si ponemos un DTO el DTO puede variar sus datos o no...Y la entidad tenemos que procurar que no...
    Bus findByCityAndDateAndHour(String city, Date date, Float hour);
}
