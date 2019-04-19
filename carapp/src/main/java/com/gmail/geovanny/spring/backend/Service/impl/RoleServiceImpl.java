package com.gmail.geovanny.spring.backend.Service.impl;

import com.gmail.geovanny.spring.backend.Service.IRoleService;
import com.gmail.geovanny.spring.backend.dao.IRoleDAO;
import com.gmail.geovanny.spring.backend.entity.Role;
import com.gmail.geovanny.spring.backend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements IRoleService {

    @Autowired
    private IRoleDAO dao;


    @Override
    public Role registrar(Role role) {
        return dao.save(role);
    }

    @Override
    public Role modificar(Role role) {
        return dao.save(role);
    }

    @Override
    public void eliminar(long id) {

    }

    @Override
    public List<Role> listar() {
        return dao.findAll();
    }

}
