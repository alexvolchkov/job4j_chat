package ru.job4j.service;

import org.springframework.stereotype.Service;
import ru.job4j.domain.Message;
import ru.job4j.repository.MessageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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

    public Message findById(int id) {
        var message = messages.findById(id);
        if (message.isEmpty()) {
            throw new NoSuchElementException(String.format("Сообщения с ID %s не найдено", id));
        }
        return message.get();
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
