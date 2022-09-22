package com.druiz.bosonit.backweb.config.security;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("api/v0")
public class LoginController {

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestHeader("mail") String mail, @RequestHeader("password") String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("mail", mail);
        headers.set("password", password);
        HttpEntity<Object> request = new HttpEntity<>(headers);
        return new RestTemplate().exchange(
                "http://backempresa:8081/api/v0/token",
                HttpMethod.GET,
                request,
                String.class);
    }

}