package com.druiz.bosonit.backempresa.user.application;


import com.druiz.bosonit.backempresa.reserva.domain.Reserva;
import com.druiz.bosonit.backempresa.reserva.infrastructure.controller.dto.ReservaOutputDto;
import com.druiz.bosonit.backempresa.user.domain.User;
import com.druiz.bosonit.backempresa.user.infrastructure.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepo userRepo;

    @Override
    public User findUserByMailAndByPassword(String email, String password) {
        return userRepo.findByMailAndPassword(email, password);
    }

    @Override
    public void saveUser(User user) {
        userRepo.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }


}



