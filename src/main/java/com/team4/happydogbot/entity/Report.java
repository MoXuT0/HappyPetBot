package com.team4.happydogbot.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.telegram.telegrambots.meta.api.objects.File;

import java.time.LocalDate;

/**
 * Класс, описывающий отчеты(reports) пользователей (adopters) о состоянии животного
 * @param 'examination' поле для статуса проверки (выполняется волонтером) отчета (по умолчанию - null)
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
    private Long reportId;
    @CreationTimestamp
    @Column(name = "report_date")
    private LocalDate reportDate;
    //поля для обработки фотографий
    private byte[] picture;
    private File filePicture;
    private String filePath;
    //поле текста при добавлении фото
    @Column(name = "report_text")
    private String caption;
    @Column(name = "examination")
    private Boolean examination = null;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chat_id")
    @JsonBackReference
    private Adopter adopter;
}
