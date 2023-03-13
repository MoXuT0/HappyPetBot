package com.team4.happydogbot.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "adopter")
public class Adopter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "chat_id", nullable = false)
    private Long chatId;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "user_name", nullable = false)
    private String userName;
    @Column(name = "telephone_number")

    private String telephoneNumber;
    private Boolean isActive;
    @CreationTimestamp
    private LocalDateTime firstLoginDate;
    @Enumerated(EnumType.STRING)
    private AdopterStatus personStatus;

}
