package com.gmail.geovanny.spring.backend.service;

import java.util.List;

public interface ICRUD<T> {

    T registrar(T t);
    T modificar(T t);
    void eliminar(long id);
    List<T> listar();

}
