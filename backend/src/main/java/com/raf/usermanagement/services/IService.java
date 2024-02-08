package com.raf.usermanagement.services;

public interface IService <T, ID>{
    <S extends T> S save(S var1);

    T findById(ID var1);

    Iterable<T> findAll();

    void deleteById(ID var1);
}
