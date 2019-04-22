package com.gmail.geovanny.spring.backend.dao;

import com.gmail.geovanny.spring.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserDAO extends JpaRepository<User, Long> {
}
