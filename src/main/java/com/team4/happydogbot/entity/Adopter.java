package com.team4.happydogbot.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    @Column(name = "id")
    private long id;
    @Column(name = "chat_id")
    private Long chatId;
    @Column(name = "first_name", nullable = false, length = 25)
    private String firstName;
    @Column(name = "last_name", nullable = false, length = 25)
    private String lastName;
    @Column(name = "user_name", nullable = false)
    private String userName;
    @Column(name = "age")
    private int age;
    @Column(name = "address", nullable = false, length = 50)
    private String address;
    @Column(name = "phone_number", nullable = false, length = 15)
    private String telephoneNumber;
    //поле для отображения уровня взаимодействия с пользователем
    //(отображает этап или состояние, в котором находится пользователь)
    @Column(name = "status", nullable = false, length = 15)
    @Enumerated(EnumType.STRING)
    Status state;

    @OneToMany(mappedBy = "adopter", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Report> reports;

    public Adopter(Long chatId, String userName, Status state) {
        this.chatId = chatId;
        this.userName = userName;
        this.state = state;
    }
}
