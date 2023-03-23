package com.team4.happydogbot.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Класс, описывающий отчеты пользователей о состоянии животного
 * @param 'examination' поле для статуса проверки (выполняется волонтером) отчета (по умолчанию - null)
 * @see AdopterDog
 * @see Dog
 * @see ReportDog
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "report_dog")
public class ReportDog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @CreationTimestamp
    @Column(name = "report_date")
    private LocalDate reportDate;
    //поля для обработки фотографий
 /*   private byte[] picture;
    private File filePicture;
    private String filePath;
    //поле текста при добавлении фото
    @Column(name = "report_text")
    private String caption;*/
    @Column(name = "examination")
    private Boolean examination = null;
    @ManyToOne
    @JoinColumn(name = "chat_id")
    @JsonBackReference
    private AdopterDog adopterDog;
}
