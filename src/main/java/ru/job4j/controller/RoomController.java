package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
        return new ResponseEntity<>(
                room.orElse(new Room()),
                room.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @GetMapping("/{id}/messsages")
    public List<Message> findAllMessageByIdRoom(@PathVariable int id) {
        return messages.findAllByRoom_Id(id);
    }

    @PostMapping("/")
    public ResponseEntity<Room> create(@RequestBody Room room) {
        return new ResponseEntity<>(
                rooms.save(room),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Room room) {
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
