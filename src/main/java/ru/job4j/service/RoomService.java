package ru.job4j.service;

import org.springframework.stereotype.Service;
import ru.job4j.domain.Room;
import ru.job4j.repository.RoomRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService {
    private final RoomRepository rooms;

    public RoomService(RoomRepository rooms) {
        this.rooms = rooms;
    }

    public List<Room> findAll() {
        List<Room> rsl = new ArrayList<>();
        rooms.findAll().forEach(rsl::add);
        return rsl;
    }

    public Optional<Room> findById(int id) {
        return rooms.findById(id);
    }

    public Room save(Room room) {
        return rooms.save(room);
    }

    public void delete(Room room) {
        rooms.delete(room);
    }
}
