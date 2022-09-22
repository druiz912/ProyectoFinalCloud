package com.druiz.bosonit.backweb;

import com.druiz.bosonit.backweb.reserva.domain.Reserva;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class BackwebApplicationTests {

	@Test
	void createReserva(){
		Reserva reserva = new Reserva();
		reserva.setId(1);
		reserva.setName("TEST");
		reserva.setSurname("TEST");
		reserva.setMail("test@example.com");
		reserva.setOriginCity("Vitoria");
		reserva.setDestinationCity("Barcelona");
		reserva.setPhone("12345");
		reserva.setDate(new Date());
		reserva.setHour(8F);
		System.out.println(reserva);
		Assertions.assertThat(reserva.getId()).isEqualTo(1);
		Assertions.assertThat(reserva.getName()).isEqualTo("TEST");
		Assertions.assertThat(reserva.getSurname()).isEqualTo("TEST");
		Assertions.assertThat(reserva.getMail()).isEqualTo("test@example.com");
		Assertions.assertThat(reserva.getOriginCity()).isEqualTo("Vitoria");
		Assertions.assertThat(reserva.getDestinationCity()).isEqualTo("Barcelona");
		Assertions.assertThat(reserva.getPhone()).isEqualTo("12345");
		Assertions.assertThat(reserva.getHour()).isEqualTo(8F);
	}
}
