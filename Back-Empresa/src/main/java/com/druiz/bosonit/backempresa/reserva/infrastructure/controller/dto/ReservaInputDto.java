package com.druiz.bosonit.backempresa.reserva.infrastructure.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaInputDto {

    @NotBlank(message = "Name can't be empty")
    private String name;

    @NotBlank(message = "Surname can't be empty")
    private String surname;

    @NotBlank(message = "Phone can't be empty")
    private String phone;

    @NotBlank(message = "City can't be empty")
    private String city;

    @NotBlank(message = "Email can't be empty")
    @Email(message = "Email property don't have correct syntax.")
    private String mail;

    @NotBlank(message = "Date can't be empty")
    private Date date;

    @NotNull(message = "Hour can't be empty")
    private Float hour;

}
