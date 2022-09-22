package com.druiz.bosonit.backempresa.reserva.domain;

import com.druiz.bosonit.backempresa.mail.domain.Mail;
import com.druiz.bosonit.backempresa.reserva.infrastructure.controller.dto.ReservaInputDto;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

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

    @NotEmpty
    @NotNull
    private String name;

    private String surname;

    private String phone;

    @NotNull
    @NotEmpty
    private String originCity;

    @NotNull
    @NotEmpty
    private String destinationCity;

    @NotNull
    @NotEmpty
    private String mail;

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date date;

    @NotEmpty
    @NotNull
    private Float hour;

    @NotEmpty
    @NotNull
    private String status;

    @OneToMany
    @JoinColumn(name="id_reserva")
    @ToString.Exclude
    private Set<Mail> emailSet;

    public Reserva(ReservaInputDto reservaInputDto) {
        this.name = reservaInputDto.getName();
        this.surname = reservaInputDto.getSurname();
        this.phone = reservaInputDto.getPhone();
        this.originCity = "Vitoria";
        this.destinationCity = reservaInputDto.getCity();
        this.mail = reservaInputDto.getMail();
        this.date = reservaInputDto.getDate();
        this.hour = reservaInputDto.getHour();
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
