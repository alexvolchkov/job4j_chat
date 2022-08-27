package ru.job4j.domain;

import ru.job4j.validation.Operation;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Positive(message = "Id должно быть больше 0",
            groups = {Operation.OnUpdate.class, Operation.OnDelete.class})
    private int id;
    @NotBlank(message = "Name должен быть не пустым")
    private String name;
    private Timestamp  created = new Timestamp(System.currentTimeMillis());

    @ManyToMany(fetch = FetchType.LAZY)
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
