package com.team4.happydogbot.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

/**
 * Class describing the user(adopter)
 * @param 'status' a field to display the level of user interaction (displays the stage or state the user is in)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "adopter")
public class Adopter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Long chatId;
    private String firstName;
    private String lastName;
    private String userName;
    private int age;
    private String address;
    private String telephoneNumber;
    //поле для отображения уровня взаимодействия с пользователем
    //(отображает этап или состояние, в котором находится пользователь)
    @Enumerated(EnumType.STRING)
    Status state;

    @JsonBackReference
    @OneToMany(mappedBy = "adopter", fetch = FetchType.EAGER)
    private List<Report> reports;

//    @JsonBackReference
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "animal_id")
//    private Animal animal;

    public Adopter(Long chatId, String userName, Status state) {
        this.chatId = chatId;
        this.userName = userName;
        this.state = state;
    }

    public Adopter(long id, Long chatId, String firstName, String lastName, String userName, int age, String address, String telephoneNumber, Status state) {
        this.id = id;
        this.chatId = chatId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.age = age;
        this.address = address;
        this.telephoneNumber = telephoneNumber;
        this.state = Status.USER;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Adopter adopter = (Adopter) o;
        return id == adopter.id && age == adopter.age && Objects.equals(chatId, adopter.chatId) && Objects.equals(firstName, adopter.firstName) && Objects.equals(lastName, adopter.lastName) && Objects.equals(userName, adopter.userName) && Objects.equals(address, adopter.address) && Objects.equals(telephoneNumber, adopter.telephoneNumber) && state == adopter.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatId, firstName, lastName, userName, age, address, telephoneNumber, state);
    }
}
