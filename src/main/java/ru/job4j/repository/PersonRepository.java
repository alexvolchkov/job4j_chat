package ru.job4j.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.domain.Person;

import java.util.Optional;

@Repository
public interface PersonRepository extends CrudRepository<Person, Integer> {
    Optional<Person> findByLogin(String login);

}
