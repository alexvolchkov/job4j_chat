package ru.job4j.controller;

import java.lang.reflect.InvocationTargetException;

public interface ModelMapper {
    void map(Object source, Object target) throws InvocationTargetException, IllegalAccessException;
}
