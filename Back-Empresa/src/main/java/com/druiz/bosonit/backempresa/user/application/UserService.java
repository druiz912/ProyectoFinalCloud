package com.druiz.bosonit.backempresa.user.application;

import com.druiz.bosonit.backempresa.user.domain.User;

import java.util.List;

public interface UserService {
    User findUserByMailAndByPassword(String mail, String password);
    void saveUser(User user);
    List<User> getAllUsers();
}
