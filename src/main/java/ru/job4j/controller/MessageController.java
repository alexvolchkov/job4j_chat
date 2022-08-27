package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.domain.Message;
import ru.job4j.service.MessageService;
import ru.job4j.service.PersonService;
import ru.job4j.service.RoomService;
import ru.job4j.validation.Operation;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@RestController
@RequestMapping("message")
public class MessageController {
    private final MessageService messages;
    private final RoomService rooms;
    private final PersonService persons;
    private final ModelMapper modelMapper;

    public MessageController(MessageService messages,
                             RoomService rooms,
                             PersonService persons,
                             ModelMapper modelMapper) {
        this.messages = messages;
        this.rooms = rooms;
        this.persons = persons;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/")
    public List<Message> findAll() {
        return messages.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> findById(@PathVariable int id) {
        var message = messages.findById(id);
        return new ResponseEntity<>(
                message, HttpStatus.OK);
    }

    @PostMapping("/{roomId}")
    @Validated(Operation.OnCreate.class)
    public ResponseEntity<Message> create(@RequestBody Message message,
                                          @PathVariable(name = "roomId") int roomId) {
        if (message.getPerson() == null) {
            throw new NullPointerException();
        }
        var room = rooms.findById(roomId);
        var person = persons.findById(message.getPerson().getId());
        if (!room.getPersons().contains(person)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Пользователь не зарегистрирован в комнате");
        }
        message.setRoom(room);
        return new ResponseEntity<>(
                messages.save(message),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    @Validated(Operation.OnUpdate.class)
    public ResponseEntity<Void> update(@Valid @RequestBody Message message) {
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
        var messageDB = messages.findById(message.getId());
        modelMapper.map(message, messageDB);
        messages.save(messageDB);
        return new ResponseEntity<>(
                messageDB, HttpStatus.OK
        );
    }
}
