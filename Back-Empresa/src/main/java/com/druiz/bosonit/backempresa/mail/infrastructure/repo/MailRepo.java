package com.druiz.bosonit.backempresa.mail.infrastructure.repo;

import com.druiz.bosonit.backempresa.mail.domain.Mail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailRepo extends JpaRepository<Mail, Integer> {
}
