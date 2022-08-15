package ru.job4j.exception;

public class PersonAlreadyExistException extends Exception {
    public PersonAlreadyExistException(String message) {
        super(message);
    }
}
