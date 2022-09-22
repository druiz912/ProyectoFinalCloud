package com.druiz.bosonit.backweb.bus.domain;

import com.druiz.bosonit.backweb.reserva.domain.Reserva;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@Table(name = "buses")
@AllArgsConstructor
@NoArgsConstructor
public class Bus {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @NotNull
    private boolean active;

    @NotNull
    @NotBlank
    private String city;

    @NotNull
    @NotBlank
    private int occupiedSeats;

    @NotNull
    @NotBlank
    private Float hour;

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date date;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_bus")
    private Set<Reserva> listReserva;

    public Bus (Integer id, boolean active, String city, Float hour, Date date, Set<Reserva> listReserva) {
        this.id = id;
        this.active = active;
        this.city = city;
        this.hour = hour;
        this.date = date;
        this.listReserva = listReserva;
        this.occupiedSeats = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Bus bus = (Bus) o;
        return id != null && Objects.equals(id, bus.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
