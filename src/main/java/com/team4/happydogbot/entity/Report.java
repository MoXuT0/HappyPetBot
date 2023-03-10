package com.team4.happydogbot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Class describing the users(adopters) report on the state of the dog
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "report")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private long id;
    @Column(name = "chat_id", nullable = false)
    private Long chatId;
    @Column(name = "report_text", nullable = false)
    private String reportText;
    @Column(name = "report_date")
    private LocalDate reportDate;
    //поля для обработки фотографий
    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Adopter adopter;
}
