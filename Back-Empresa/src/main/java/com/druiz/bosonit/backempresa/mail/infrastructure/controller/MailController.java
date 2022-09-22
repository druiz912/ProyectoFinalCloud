package com.druiz.bosonit.backempresa.mail.infrastructure.controller;

import com.druiz.bosonit.backempresa.mail.application.MailService;
import com.druiz.bosonit.backempresa.mail.infrastructure.controller.dto.MailInputDto;
import com.druiz.bosonit.backempresa.mail.infrastructure.controller.dto.MailOutputDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("api/v0/mails")
public class MailController {

    @Autowired
    MailService mailService;

    @GetMapping
    public ResponseEntity<?> getMails(
            @RequestParam(required = false, defaultValue = "noCity") String city,
            @RequestParam String lowerDate,
            @RequestParam String upperDate,
            @RequestParam(required = false, defaultValue = "-1") Float lowerHour,
            @RequestParam(required = false, defaultValue = "-1") Float upperHour
    ) {
        HashMap<String, Object> conditions = new HashMap<>();
        conditions.put("city", city);
        conditions.put("lowerDate", lowerDate);
        conditions.put("upperDate", upperDate);
        conditions.put("lowerHour", lowerHour);
        conditions.put("upperHour", upperHour);

        List<MailOutputDto> mailList = mailService.getMailsByConditions(conditions);

        return new ResponseEntity<>(mailList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MailOutputDto> resendMail(@RequestBody @Valid MailInputDto mailInputDTO) {
        MailOutputDto mailOutputDTO = mailService.resend(mailInputDTO);
        return new ResponseEntity<>(mailOutputDTO, HttpStatus.OK);
    }


}
