package com.team4.happydogbot.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import javax.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * Класс, описывающий пользователя (adopter)
 *
 * @param 'status' поле для отображения уровня взаимодействия с пользователем
 * (отображает этап или состояние, в котором находится пользователь)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "adopter")
public class Adopter {
    @Id
    @Column(name = "adopter_id")
    private Long chatId;
    @Column(name = "first_name", length = 25)
    private String firstName;
    @Column(name = "last_name", length = 25)
    private String lastName;
    @Column(name = "user_name", length = 25)
    private String userName;
    @Column(name = "age")
    private int age;
    @Column(name = "address", length = 50)
    private String address;
    @Column(name = "phone_number", length = 15)
    private String telephoneNumber;
    //поле для отображения уровня взаимодействия с пользователем
    //(отображает этап или состояние, в котором находится пользователь)
    @CreationTimestamp
    private LocalDateTime firstLoginDate;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    Status state;

    @OneToMany(mappedBy = "adopter", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
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

    public Adopter(Long chatId, String firstName, String lastName, String userName, int age, String address,
                   String telephoneNumber, Status state) {
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
        return age == adopter.age && Objects.equals(chatId, adopter.chatId)
                && Objects.equals(firstName, adopter.firstName) && Objects.equals(lastName, adopter.lastName)
                && Objects.equals(userName, adopter.userName) && Objects.equals(address, adopter.address)
                && Objects.equals(telephoneNumber, adopter.telephoneNumber) && state == adopter.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, firstName, lastName, userName, age, address, telephoneNumber, state);
    }
}
