package com.druiz.bosonit.backempresa.user.infrastructure.repo;

import com.druiz.bosonit.backempresa.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Integer> {
    User findByMailAndPassword(String email, String password);
}
