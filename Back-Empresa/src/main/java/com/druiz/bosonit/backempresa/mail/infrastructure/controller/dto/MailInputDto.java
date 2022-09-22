package com.druiz.bosonit.backempresa.mail.infrastructure.controller.dto;

import com.druiz.bosonit.backempresa.mail.domain.Mail;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class MailInputDto {
    @NotBlank(message = "City can't be empty")
    private String city;

    @NotBlank(message = "Email can't be empty")
    private String mail;

    @NotBlank(message = "Date can't be empty")
    private String date;

    @NotNull(message = "Hour can't be empty")
    private Float hour;

    public Mail convertToEntity() {
        return new Mail();
    }
}
