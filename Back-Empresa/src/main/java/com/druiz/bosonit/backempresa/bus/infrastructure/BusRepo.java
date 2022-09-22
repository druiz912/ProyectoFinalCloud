package com.druiz.bosonit.backempresa.bus.infrastructure;

import com.druiz.bosonit.backempresa.bus.domain.Bus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface BusRepo extends JpaRepository<Bus, Integer> {
    Bus findByCityAndDateAndHour(String city, Date date, Float hour);
}