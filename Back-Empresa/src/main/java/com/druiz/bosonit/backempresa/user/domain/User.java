package com.druiz.bosonit.backempresa.user.domain;

import com.druiz.bosonit.backempresa.user.infrastructure.controller.dto.UserInputDto;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @NotNull
    private String mail;

    @NotNull
    private String password;

    @NotNull
    private String role;

    public User(UserInputDto userInputDto) {
        mail = userInputDto.getMail();
        password = userInputDto.getPassword();
        role = userInputDto.getRole();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}