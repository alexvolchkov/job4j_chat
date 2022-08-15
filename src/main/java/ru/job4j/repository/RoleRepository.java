package ru.job4j.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.domain.Role;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {

    Optional<Role> findByName(String name);
}
