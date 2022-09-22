package com.druiz.bosonit.backempresa.mail.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

import static com.druiz.bosonit.backempresa.config.security.Constants.VIRTUALTRAVEL2;
import static com.druiz.bosonit.backempresa.config.security.Constants.VIRTUALTRAVELMAIL;

@Service
@Slf4j
public class SendMail {


    public static void sendMail(String recipient, String sender, String subject, String messageSended, String condition) {

        String host = "smtp.gmail.com";

        Properties properties = System.getProperties();

        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "${spring.mail.port}");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // Usuario gmail y contraseña generada
                return new PasswordAuthentication(VIRTUALTRAVELMAIL, VIRTUALTRAVEL2);
            }
        });

        // Used to debug SMTP issues
        session.setDebug(true);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject(subject);

            if (condition.equals("html")) {
                message.setContent(messageSended,
                        "text/html");
            } else {
                // Now set the actual message
                message.setText(messageSended);
            }
            log.info("Sending...");
            // Send message
            Transport.send(message); // Error --> bad configure gmail
            log.info("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }

    }

}
