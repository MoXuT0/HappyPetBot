package com.team4.happydogbot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Class describing the user(adopter)
 *
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
    @Column(name = "age")
    private int age;
    @Column(name = "phone_number", nullable = false, length = 15)
    private String telephoneNumber;
    @Column(name = "status", nullable = false, length = 15)
    @Enumerated(EnumType.STRING)
    Enum<Status> state;
    @OneToMany(mappedBy = "adopter", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Report> reports;
}
