package com.team4.happydogbot.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @Column(name = "adopter_id")
    private Long chatId;
    @Column(name = "first_name", nullable = true)
    private String firstName;
    @Column(name = "last_name", nullable = true)
    private String lastName;
    @Column(name = "user_name", nullable = true)
    private String userName;
    @Column(name = "age")
    private int age;
    @Column(name = "address")
    private String address;
    @Column(name = "phone_number")
    private String telephoneNumber;
    @Column(name = "status")
    //поле для отображения уровня взаимодействия с пользователем
    //(отображает этап или состояние, в котором находится пользователь)
    @Enumerated(EnumType.STRING)
    Status state;

    @OneToMany(mappedBy = "adopterDog", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ReportDog> reportDogs;

//    @OneToOne(fetch = FetchType.EAGER)
//    @JsonBackReference
//    @JoinColumn(name = "dog_id")
    @OneToOne(mappedBy = "adopterDog", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Dog dog;

    public AdopterDog(Long chatId, String firstName, String lastName, String userName, int age, String address,
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
