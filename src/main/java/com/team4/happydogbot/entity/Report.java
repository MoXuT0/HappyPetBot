package com.team4.happydogbot.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.CreationTimestamp;
import org.telegram.telegrambots.meta.api.objects.File;

import java.time.LocalDate;

/**
 * Class describing the users(adopters) report on the state of the dog
 * @param 'examination' field for the verification status (performed by a volunteer) of the report (default - null)
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
//    @CreationTimestamp
    @Column(name = "report_date")
    private LocalDate reportDate;
    //поля для обработки фотографий
    private byte[] picture;
    private File filePicture;
    private String filePath;
    //поле текста при добавлении фото
    @Column(name = "report_text", nullable = false)
    private String caption;
    //поле для статуса проверки(выполняется волонтером) отчета (по умолчанию - null)
    @Column(name = "examination")
    private Boolean examination;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Adopter adopter;
}
