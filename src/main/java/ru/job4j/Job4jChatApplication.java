package ru.job4j;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.job4j.controller.ModelMapper;
import ru.job4j.controller.SimpleModelMapper;

@SpringBootApplication
public class Job4jChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(Job4jChatApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new SimpleModelMapper();
    }
}
