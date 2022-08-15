package ru.job4j.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.domain.Room;

@Repository
public interface RoomRepository extends CrudRepository<Room, Integer> {
}
