package ru.job4j.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.job4j.domain.Person;
import ru.job4j.exception.PersonAlreadyExistException;
import ru.job4j.service.PersonService;
import ru.job4j.validation.Operation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {
    private final PersonService persons;
    private final BCryptPasswordEncoder encoder;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;

    public PersonController(PersonService persons,
                            BCryptPasswordEncoder encoder,
                            ObjectMapper objectMapper,
                            ModelMapper modelMapper) {
        this.persons = persons;
        this.encoder = encoder;
        this.objectMapper = objectMapper;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/")
    public ResponseEntity<List<Person>> findAll() {
        return new ResponseEntity<>(
                persons.findAll(),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        var person = this.persons.findById(id);
        return new ResponseEntity<>(
                person, HttpStatus.OK);
    }

    @PostMapping("/sign-up")
    @Validated(Operation.OnCreate.class)
    public ResponseEntity<Person> create(@Valid @RequestBody Person person) throws PersonAlreadyExistException {
        person.setPassword(encoder.encode(person.getPassword()));
        return new ResponseEntity<>(
                persons.save(person),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    @Validated(Operation.OnUpdate.class)
    public ResponseEntity<Void> update(@Valid @RequestBody Person person) throws PersonAlreadyExistException {
        this.persons.save(person);
            return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Person person = new Person();
        person.setId(id);
        persons.delete(person);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(value = PersonAlreadyExistException.class)
    public void handleException(PersonAlreadyExistException exception,
                                HttpServletRequest request,
                                HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() { {
            put("message", exception.getMessage());
            put("type", exception.getClass());
        }}));
    }

    @PatchMapping("/")
    public ResponseEntity<Person> patchMessage(@RequestBody Person person)
            throws InvocationTargetException, IllegalAccessException, PersonAlreadyExistException {
        var current = persons.findById(person.getId());
        modelMapper.map(person, current);
        persons.save(current);
        return new ResponseEntity<>(
                current, HttpStatus.OK
        );
    }
}
