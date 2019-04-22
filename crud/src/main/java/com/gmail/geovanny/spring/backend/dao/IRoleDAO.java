package com.gmail.geovanny.spring.backend.dao;

import com.gmail.geovanny.spring.backend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoleDAO extends JpaRepository<Role, Long> {
}
