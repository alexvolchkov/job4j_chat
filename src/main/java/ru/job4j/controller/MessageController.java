package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.domain.Message;
import ru.job4j.domain.Person;
import ru.job4j.domain.Room;
import ru.job4j.service.MessageService;
import ru.job4j.service.PersonService;
import ru.job4j.service.RoomService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("message")
public class MessageController {
    private final MessageService messages;
    private final RoomService rooms;
    private final PersonService persons;

    public MessageController(MessageService messages, RoomService rooms, PersonService persons) {
        this.messages = messages;
        this.rooms = rooms;
        this.persons = persons;
    }

    @GetMapping("/")
    public List<Message> findAll() {
        return messages.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> findById(@PathVariable int id) {
        var message = messages.findById(id);
        if (message.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("Сообщения с ID %s не найдено", id));
        }
        return new ResponseEntity<>(
                message.get(), HttpStatus.OK);
    }

    @PostMapping("/{roomId}")
    public ResponseEntity<Message> create(@RequestBody Message message, @PathVariable int roomId) {
        if (message == null || message.getPerson() == null) {
            throw new NullPointerException();
        }
        Optional<Room> room = rooms.findById(roomId);
        Optional<Person> person = persons.findById(message.getPerson().getId());
        if (room.isEmpty() || person.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Комната или пользователь не найден");
        }
        message.setRoom(room.get());
        return new ResponseEntity<>(
                messages.save(message),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Message message) {
        if (message == null) {
            throw new NullPointerException();
        }
        messages.save(message);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Message message = new Message();
        message.setId(id);
        messages.delete(message);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/")
    public ResponseEntity<Message> patchMessage(@RequestBody Message message)
            throws InvocationTargetException, IllegalAccessException {
        var optionalCurrent = messages.findById(message.getId());
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
                var newValue = getMethod.invoke(message);
                if (newValue != null) {
                    setMethod.invoke(current, newValue);
                }
            }
        }
        messages.save(current);
        return new ResponseEntity<>(
                current, HttpStatus.OK
        );
    }
}
