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
 * @param 'status' поле для отображения уровня взаимодействия с пользователем
 * (отображает этап или состояние, в котором находится пользователь)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "adopter_cat")
public class AdopterCat {
    @Id
    @Column(name = "chat_id")
    private Long chatId;
    @Column(name = "first_name", nullable = false, length = 25)
    private String firstName;
    @Column(name = "last_name", nullable = false, length = 25)
    private String lastName;
    @Column(name = "user_name", nullable = false, length = 25)
    private String userName;
    @Column(name = "age")
    private int age;
    @Column(name = "address", nullable = false, length = 50)
    private String address;
    @Column(name = "phone_number", nullable = false, length = 15)
    private String telephoneNumber;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status state;

    @OneToMany(mappedBy = "adopterCat", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ReportCat> reports;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Cat cat;

    public AdopterCat(Long chatId, String firstName, String lastName, String userName, int age, String address,
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
        AdopterCat adopter = (AdopterCat) o;
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
