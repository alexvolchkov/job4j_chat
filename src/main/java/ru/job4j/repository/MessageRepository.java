package ru.job4j.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.domain.Message;

@Repository
public interface MessageRepository extends CrudRepository<Message, Integer> {

    Iterable<Message> findAllByRoom_Id(int id);
}
