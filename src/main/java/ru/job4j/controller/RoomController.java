package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.domain.Message;
import ru.job4j.domain.Room;
import ru.job4j.service.MessageService;
import ru.job4j.service.RoomService;

import java.util.List;

@RestController
@RequestMapping("room")
public class RoomController {
    private final RoomService rooms;
    private final MessageService messages;

    public RoomController(RoomService rooms, MessageService messages) {
        this.rooms = rooms;
        this.messages = messages;
    }

    @GetMapping("/")
    public List<Room> findAll() {
        return rooms.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> findById(@PathVariable int id) {
        var room = this.rooms.findById(id);
        if (room.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("Комната с ID %s не найдена", id));
        }
        return new ResponseEntity<>(
                room.get(), HttpStatus.OK);
    }

    @GetMapping("/{id}/messsages")
    public List<Message> findAllMessageByIdRoom(@PathVariable int id) {
        return messages.findAllByRoom_Id(id);
    }

    @PostMapping("/")
    public ResponseEntity<Room> create(@RequestBody Room room) {
        if (room == null) {
            throw new NullPointerException();
        }
        return new ResponseEntity<>(
                rooms.save(room),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Room room) {
        if (room == null) {
            throw new NullPointerException();
        }
        rooms.save(room);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Room room = new Room();
        room.setId(id);
        rooms.delete(room);
        return ResponseEntity.ok().build();
    }
}
