package com.druiz.bosonit.backempresa.user.infrastructure.controller;

import com.druiz.bosonit.backempresa.user.application.UserService;
import com.druiz.bosonit.backempresa.user.domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v0")
public class UserController {

    @Autowired
    UserService userService;

    // localhost:8081/api/v0/token
    @GetMapping("/token")
    public ResponseEntity<?> getToken(@RequestHeader("mail") String mail, @RequestHeader("password") String password ) {
        User userWantedToFind = userService.findUserByMailAndByPassword(mail, password);
        if (userWantedToFind == null) return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);

        if (!userWantedToFind.getMail().trim().equals(mail) || !userWantedToFind.getPassword().equals(password))
            return new ResponseEntity<>("No coinciden las credenciales", HttpStatus.CONFLICT);

        String role = userWantedToFind.getRole().toUpperCase().trim();
        return new ResponseEntity<>(getJWTToken(mail, role), HttpStatus.OK);
    }

    private String getJWTToken(String mail, String role) {
        String secretKey = "secret";
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");

        String token = Jwts
                .builder()
                .setId("jwt")
                .setSubject(mail)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1800000)) // Half hour
                .signWith(SignatureAlgorithm.HS512,
                        secretKey.getBytes()).compact();

        return "Bearer Token: " + token;
    }

    @GetMapping("users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }


}
