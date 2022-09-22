package com.druiz.bosonit.backweb.reserva.domain;

import com.druiz.bosonit.backweb.reserva.infrastructure.controller.dto.ReservaInputDto;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@Table(name = "reservas")
@AllArgsConstructor
@NoArgsConstructor
public class Reserva {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @NotBlank(message = "Name cant' be blank")
    private String name;

    @NotBlank(message = "Surname can't be blank")
    private String surname;

    @NotBlank(message = "Phone can't be blank")
    private String phone;

    @NotBlank(message = "OriginCity can't be blank")
    private String originCity;


    @NotBlank(message = "DestinationCity can't be blank")
    private String destinationCity;

    @NotEmpty(message = "Email can't be empty")
    @Email(message = "Bad syntax email")
    private String mail;

    @NotBlank(message = "Date can't be blank")
    @Temporal(TemporalType.DATE)
    private Date date;

    @NotBlank(message = "Hour can't be blank")
    private Float hour;

    @NotBlank(message = "Status can't be blank")
    private String status;

    public Reserva(ReservaInputDto reservaInputDto) {
        this.name = reservaInputDto.getName();
        this.surname = reservaInputDto.getSurname();
        this.phone = reservaInputDto.getPhone();
        this.originCity = "Vitoria"; //Vitoria
        this.destinationCity = reservaInputDto.getCity(); //· Valencia,Madrid,Barcelona,Bilbao
        this.mail = reservaInputDto.getMail();
        this.date = reservaInputDto.getDate();
        this.hour = reservaInputDto.getHour(); // 8,12,16,20
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Reserva reserva = (Reserva) o;
        return id != null && Objects.equals(id, reserva.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
