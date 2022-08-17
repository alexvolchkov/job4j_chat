package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.domain.Message;
import ru.job4j.domain.Room;
import ru.job4j.service.MessageService;
import ru.job4j.service.RoomService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
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

    @PatchMapping("/")
    public ResponseEntity<Room> patchRoom(@RequestBody Room room) throws InvocationTargetException, IllegalAccessException {
        var optionalCurrent = rooms.findById(room.getId());
        if (optionalCurrent.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        var current = optionalCurrent.get();
        var methods = current.getClass().getDeclaredMethods();
        var namePerMethod = new HashMap<String, Method>();
        for (var method : methods) {
            var name = method.getName();
            if (name.startsWith("get") || name.startsWith("set")) {
                namePerMethod.put(name, method);
            }
        }
        for (var name : namePerMethod.keySet()) {
            if (name.startsWith("get")) {
                var getMethod = namePerMethod.get(name);
                var setMethod = namePerMethod.get(name.replace("get", "set"));
                if (setMethod == null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Impossible invoke set method from object : " + current + ", Check set and get pairs.");

                }
                var newValue = getMethod.invoke(room);
                if (newValue != null) {
                    setMethod.invoke(current, newValue);
                }
            }
        }
        rooms.save(current);
        return new ResponseEntity<>(
                current, HttpStatus.OK
        );
    }
}
