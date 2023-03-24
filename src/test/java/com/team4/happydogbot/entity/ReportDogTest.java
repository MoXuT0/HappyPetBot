package com.team4.happydogbot.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

/**
 * Тест - класс для проверки создания отчета о собаке
 * @see ReportDog
 * @see ReportDogTest
 */
public class ReportDogTest {

    private final Long id = 1L;
    private final LocalDate reportDate = LocalDate.of(2023, 3, 24);
    private final String fileId = "Test124578";
    private final String caption = "Рацион: гуд; Самочувствие: гуд; Поведение: гуд";

    ReportDog reportDog = new ReportDog(id, reportDate, fileId, caption);

    @Test
    @DisplayName("Проверка на наличие данных при создании отчета о коте")
    public void createReportCatTest() {
        Long reportId = reportDog.getId();
        LocalDate reportDateDog = reportDog.getReportDate();
        String reportFileId = reportDog.getFileId();
        String reportCaption = reportDog.getCaption();

        Assertions.assertEquals(id, reportId);
        Assertions.assertEquals(reportDate, reportDateDog);
        Assertions.assertEquals(fileId, reportFileId);
        Assertions.assertEquals(caption, reportCaption);
    }

    @Test
    @DisplayName("Проверка на отсутствие переданных данных при создании отчета о коте")
    public void createReportCatNullTest() {
        ReportCat reportDogTest = new ReportCat();
        Long reportId = reportDogTest.getId();
        LocalDate reportDateCat = reportDogTest.getReportDate();
        String reportFileId = reportDogTest.getFileId();
        String reportCaption = reportDogTest.getCaption();

        Assertions.assertNull(reportId);
        Assertions.assertNull(reportDateCat);
        Assertions.assertNull(reportFileId);
        Assertions.assertNull(reportCaption);
    }
}
