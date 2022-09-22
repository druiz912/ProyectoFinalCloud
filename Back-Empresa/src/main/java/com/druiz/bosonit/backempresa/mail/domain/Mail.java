package com.druiz.bosonit.backempresa.mail.domain;

import com.druiz.bosonit.backempresa.mail.infrastructure.controller.dto.MailOutputDto;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name="correos_enviados")
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Mail {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @NotNull
    private String sender;

    @NotNull
    private String receiver;

    @NotNull
    private String subject;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Mail mail = (Mail) o;
        return id != null && Objects.equals(id, mail.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}