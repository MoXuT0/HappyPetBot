package com.team4.happydogbot.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@Table(name = "adopter_dog")
public class AdopterDog {
    @Id
    @Column(name = "chat_id", nullable = false)
    private Long chatId;
    @Column(name = "first_name", nullable = false, length = 25)
    private String firstName;
    @Column(name = "last_name", length = 25)
    private String lastName;
    @Column(name = "user_name", nullable = false, length = 25)
    private String userName;
    @Column(name = "age")
    private int age;
    @Column(name = "address", length = 50)
    private String address;
    @Column(name = "phone_number", nullable = false, length = 15)
    private String telephoneNumber;
    @Column(name = "status")
    //поле для отображения уровня взаимодействия с пользователем
    //(отображает этап или состояние, в котором находится пользователь)
    @Enumerated(EnumType.STRING)
    private Status state = Status.REGISTRATION;

    @OneToMany(mappedBy = "adopterDog", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ReportDog> reports;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "dog_id")
    private Dog dog;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdopterDog adopterDog = (AdopterDog) o;
        return age == adopterDog.age && Objects.equals(chatId, adopterDog.chatId)
                && Objects.equals(firstName, adopterDog.firstName) && Objects.equals(lastName, adopterDog.lastName)
                && Objects.equals(userName, adopterDog.userName) && Objects.equals(address, adopterDog.address)
                && Objects.equals(telephoneNumber, adopterDog.telephoneNumber) && state == adopterDog.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, firstName, lastName, userName, age, address, telephoneNumber, state);
    }
}
