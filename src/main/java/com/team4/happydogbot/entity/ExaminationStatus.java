package com.team4.happydogbot.entity;

/**
 * Класс, содержащий параметры для всех состояний
 * @see ReportCat
 * @see ReportDog
 */
public enum ExaminationStatus {
    ACCEPTED ("Отчет проверен и принят"),
    UNCHECKED ("Отчет не проверен"),
    REJECTED ("Отчет проверен и отправлен на доработку");

    private final String examinationStatus;

    ExaminationStatus(String examinationStatus) {
        this.examinationStatus = examinationStatus;
    }
}
