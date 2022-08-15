package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.domain.Message;
import ru.job4j.domain.Person;
import ru.job4j.domain.Room;
import ru.job4j.service.MessageService;
import ru.job4j.service.PersonService;
import ru.job4j.service.RoomService;

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
        return new ResponseEntity<>(
                message.orElse(new Message()),
                message.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PostMapping("/{roomId}")
    public ResponseEntity<Message> create(@RequestBody Message message, @PathVariable int roomId) {
        var rsl = ResponseEntity.badRequest().body(new Message());
        Optional<Room> room = rooms.findById(roomId);
        Optional<Person> person = persons.findById(message.getPerson().getId());
        if (room.isPresent() && person.isPresent()) {
            message.setRoom(room.get());
            rsl = new ResponseEntity<>(
                    messages.save(message),
                    HttpStatus.CREATED
            );
        }
        return rsl;
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Message message) {
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
}
