package com.druiz.bosonit.backempresa.user.infrastructure.controller.dto;

import com.druiz.bosonit.backempresa.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInputDto {
    @NotBlank
    private String mail;

    @NotBlank
    private String password;

    private String role = "ROLE_USER";

    public User convertEntityToDTO() {
        return new User(null, this.mail, this.password, this.role);
    }
}
