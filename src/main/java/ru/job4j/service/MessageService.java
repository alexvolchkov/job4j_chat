package ru.job4j.service;

import org.springframework.stereotype.Service;
import ru.job4j.domain.Message;
import ru.job4j.repository.MessageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    private final MessageRepository messages;

    public MessageService(MessageRepository messages) {
        this.messages = messages;
    }

    public List<Message> findAll() {
        List<Message> rsl = new ArrayList<>();
        messages.findAll().forEach(rsl::add);
        return rsl;
    }

    public Optional<Message> findById(int id) {
        return messages.findById(id);
    }

    public List<Message> findAllByRoom_Id(int id) {
        List<Message> rsl = new ArrayList<>();
        messages.findAllByRoom_Id(id).forEach(rsl::add);
        return rsl;
    }

    public Message save(Message message) {
        return messages.save(message);
    }

    public void delete(Message message) {
        messages.delete(message);
    }
}
