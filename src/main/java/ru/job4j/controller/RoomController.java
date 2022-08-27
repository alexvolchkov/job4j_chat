package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.job4j.domain.Message;
import ru.job4j.domain.Room;
import ru.job4j.service.MessageService;
import ru.job4j.service.PersonService;
import ru.job4j.service.RoomService;
import ru.job4j.validation.Operation;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@RestController
@RequestMapping("room")
public class RoomController {
    private final RoomService rooms;
    private final MessageService messages;
    private final PersonService persons;
    private final ModelMapper modelMapper;

    public RoomController(RoomService rooms,
                          MessageService messages,
                          PersonService persons,
                          ModelMapper modelMapper) {
        this.rooms = rooms;
        this.messages = messages;
        this.persons = persons;
        this.modelMapper = modelMapper;
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
        modelMapper.map(room, current);
        rooms.save(current);
        return new ResponseEntity<>(
                current, HttpStatus.OK
        );
    }
}
