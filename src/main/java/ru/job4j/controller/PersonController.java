package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.domain.Person;
import ru.job4j.exception.PersonAlreadyExistException;
import ru.job4j.service.PersonService;

import java.util.List;

@RestController
@RequestMapping("person")
public class PersonController {
    private final PersonService persons;

    public PersonController(PersonService persons) {
        this.persons = persons;
    }

    @GetMapping("/")
    public List<Person> findAll() {
        return persons.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        var person = this.persons.findById(id);
        return new ResponseEntity<>(
                person.orElse(new Person()),
                person.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PostMapping("/")
    public ResponseEntity<Person> create(@RequestBody Person person) {
        try {
            return new ResponseEntity<>(
                    persons.save(person),
                    HttpStatus.CREATED
            );
        } catch (PersonAlreadyExistException e) {
            e.printStackTrace();
            return  ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Person person) {
        try {
            this.persons.save(person);
            return ResponseEntity.ok().build();
        } catch (PersonAlreadyExistException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Person person = new Person();
        person.setId(id);
        persons.delete(person);
        return ResponseEntity.ok().build();
    }
}
