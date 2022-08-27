package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.domain.Message;
import ru.job4j.domain.Room;
import ru.job4j.service.MessageService;
import ru.job4j.service.PersonService;
import ru.job4j.service.RoomService;
import ru.job4j.validation.Operation;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("room")
public class RoomController {
    private final RoomService rooms;
    private final MessageService messages;
    private final PersonService persons;

    public RoomController(RoomService rooms, MessageService messages, PersonService persons) {
        this.rooms = rooms;
        this.messages = messages;
        this.persons = persons;
    }

    @GetMapping("/")
    public List<Room> findAll() {
        return rooms.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> findById(@PathVariable int id) {
        var room = this.rooms.findById(id);
        return new ResponseEntity<>(
                room, HttpStatus.OK);
    }

    @GetMapping("/{id}/messsages")
    public List<Message> findAllMessageByIdRoom(@PathVariable int id) {
        return messages.findAllByRoom_Id(id);
    }

    @PostMapping("/")
    @Validated(Operation.OnCreate.class)
    public ResponseEntity<Room> create(@Valid @RequestBody Room room) {
        return new ResponseEntity<>(
                rooms.save(room),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/{roomId}/addPerson/{personId}")
    public ResponseEntity<Room> addPerson(@PathVariable(name = "roomId") int roomId,
                                          @PathVariable(name = "personId") int personId) {
        var room = rooms.findById(roomId);
        var person = persons.findById(personId);
        room.addPerson(person);
        return new ResponseEntity<>(
                rooms.save(room),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{roomId}/addPerson/{personId}")
    public ResponseEntity<Void> deletePerson(@PathVariable(name = "roomId") int roomId,
                                          @PathVariable(name = "personId") int personId) {
        var room = rooms.findById(roomId);
        var person = persons.findById(personId);
        room.getPersons().remove(person);
        rooms.save(room);
        return  ResponseEntity.ok().build();
    }

    @PutMapping("/")
    @Validated(Operation.OnUpdate.class)
    public ResponseEntity<Void> update(@Valid @RequestBody Room room) {
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
        var current = rooms.findById(room.getId());
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
