package ru.job4j.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.job4j.domain.Role;
import ru.job4j.exception.PersonAlreadyExistException;
import ru.job4j.repository.PersonRepository;
import ru.job4j.domain.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PersonService implements UserDetailsService {
    private final PersonRepository persons;
    private final RoleService roles;

    public PersonService(PersonRepository persons, RoleService roles) {
        this.persons = persons;
        this.roles = roles;
    }

    public List<Person> findAll() {
        List<Person> rsl = new ArrayList<>();
        persons.findAll().forEach(rsl::add);
        return rsl;
    }

    public Person save(Person person) throws PersonAlreadyExistException {
        if (persons.findByLogin(person.getLogin()).isPresent()) {
            throw new PersonAlreadyExistException(
                    String.format("Пользоватеь уже существует с именем %s", person.getLogin()));
        }
        Role role = roles.findByName("ROLE_USER").get();
        person.addRole(role);
        return persons.save(person);
    }

    public void delete(Person person) {
        persons.delete(person);
    }

    public Optional<Person> findById(int id) {
        return persons.findById(id);
    }

    public Optional<Person> findByLogin(String login) {
        return persons.findByLogin(login);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return persons.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username does not exist"));
    }
}
