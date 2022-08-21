package ru.job4j.domain;

import ru.job4j.validation.Operation;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "Id must be non null",
            groups = {Operation.OnUpdate.class, Operation.OnDelete.class})
    private int id;
    @NotBlank(message = "Name must be not empty")
    private String name;
    //@PastOrPresent(message = "Created не должна быть будующей датой")
    private Timestamp  created = new Timestamp(System.currentTimeMillis());

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "room_person", joinColumns = {
            @JoinColumn(name = "room_id", nullable = false, updatable = false)},
    inverseJoinColumns = {
            @JoinColumn(name = "person_id", nullable = false, updatable = false)})
    private Set<Person> persons;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Set<Person> getPersons() {
        return persons;
    }

    public void setPersons(Set<Person> persons) {
        this.persons = persons;
    }

    public void addPerson(Person person) {
        this.persons.add(person);
    }
}
