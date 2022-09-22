package com.druiz.bosonit.backempresa.mail.application;

import com.druiz.bosonit.backempresa.mail.domain.Mail;
import com.druiz.bosonit.backempresa.mail.infrastructure.controller.dto.MailInputDto;
import com.druiz.bosonit.backempresa.mail.infrastructure.controller.dto.MailOutputDto;

import java.util.HashMap;
import java.util.List;

public interface MailService {
    void postMail(Mail mailReceived);
    List<MailOutputDto> getMailsByConditions(HashMap<String, Object> conditions);
    MailOutputDto resend(MailInputDto mail);

}
