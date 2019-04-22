package com.gmail.geovanny.spring.backend.service.impl;

import com.gmail.geovanny.spring.backend.dao.IUserDAO;
import com.gmail.geovanny.spring.backend.entity.User;
import com.gmail.geovanny.spring.backend.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserDAO dao;

    @Override
    public User registrar(User user) {
        return dao.save(user);
    }

    @Override
    public User modificar(User user) {
        return dao.save(user);
    }

    @Override
    public void eliminar(long id) {
         dao.deleteById(id);
    }

    @Override
    public List<User> listar() {
        return dao.findAll();
    }
}
