package com.team4.happydogbot.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

/**
 * Тест - класс для проверки создания отчета о коте
 * @see ReportCat
 * @see ReportCatTest
 */
public class ReportCatTest {

    private final Long id = 1L;
    private final LocalDate reportDate = LocalDate.of(2023, 3, 24);
    private final String fileId = "Test124578";
    private final String caption = "Рацион: гуд; Самочувствие: гуд; Поведение: гуд";

    ReportCat reportCat = new ReportCat(id, reportDate, fileId, caption);

    @Test
    @DisplayName("Проверка на наличие данных при создании отчета о коте")
    public void createReportCatTest() {
        Long reportId = reportCat.getId();
        LocalDate reportDateCat = reportCat.getReportDate();
        String reportFileId = reportCat.getFileId();
        String reportCaption = reportCat.getCaption();

        Assertions.assertEquals(id, reportId);
        Assertions.assertEquals(reportDate, reportDateCat);
        Assertions.assertEquals(fileId, reportFileId);
        Assertions.assertEquals(caption, reportCaption);
    }

    @Test
    @DisplayName("Проверка на отсутствие переданных данных при создании отчета о коте")
    public void createReportCatNullTest() {
        ReportCat reportCatTest = new ReportCat();
        Long reportId = reportCatTest.getId();
        LocalDate reportDateCat = reportCatTest.getReportDate();
        String reportFileId = reportCatTest.getFileId();
        String reportCaption = reportCatTest.getCaption();

        Assertions.assertNull(reportId);
        Assertions.assertNull(reportDateCat);
        Assertions.assertNull(reportFileId);
        Assertions.assertNull(reportCaption);
    }
}
