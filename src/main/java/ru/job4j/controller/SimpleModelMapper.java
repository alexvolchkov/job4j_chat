package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class SimpleModelMapper implements ModelMapper {
    @Override
    public void map(Object source, Object target) throws InvocationTargetException, IllegalAccessException {
        var methods = target.getClass().getDeclaredMethods();
        var namePerMethod = new HashMap<String, Method>();
        for (var method : methods) {
            var name = method.getName();
            if (name.startsWith("get") || name.startsWith("set")) {
                namePerMethod.put(name, method);
            }
        }
        for (var name : namePerMethod.keySet()) {
            if (name.startsWith("get")) {
                var getMethod = namePerMethod.get(name);
                var setMethod = namePerMethod.get(name.replace("get", "set"));
                if (setMethod == null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Impossible invoke set method from object : " + target + ", Check set and get pairs.");

                }
                var newValue = getMethod.invoke(source);
                if (newValue != null) {
                    setMethod.invoke(target, newValue);
                }
            }
        }
    }
}
