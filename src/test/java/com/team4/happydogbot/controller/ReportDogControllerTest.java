package com.team4.happydogbot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team4.happydogbot.entity.ExaminationStatus;
import com.team4.happydogbot.entity.ReportDog;
import com.team4.happydogbot.exception.ReportDogNotFoundException;
import com.team4.happydogbot.service.ReportDogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Тест - класс для проверки API endpoints при обращении к маршрутам отдельными HTTP методами
 * для класса - сервиса отчетов о собаках
 * @see ReportDog
 * @see ReportDogService
 * @see ReportDogController
 * @see ReportDogControllerTest
 */
@WebMvcTest(ReportDogController.class)
public class ReportDogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ReportDogService reportDogService;

    private final ReportDog expected = new ReportDog();
    private final ReportDog expected1 = new ReportDog();
    private final ReportDog actual = new ReportDog();
    private final ReportDog exceptionReportDog = new ReportDog();

    @BeforeEach
    public void setUp() {
        expected.setId(1L);
        expected.setReportDate(LocalDate.of(2023, 3, 24));
        expected.setFileId("Test124578");
        expected.setCaption("Рацион: гуд; Самочувствие: гуд; Поведение: гуд");
        expected.setExamination(ExaminationStatus.UNCHECKED);

        expected1.setId(2L);
        expected1.setReportDate(LocalDate.of(2023, 3, 24));
        expected1.setFileId("Test986532");
        expected1.setCaption("Рацион: гуд; Самочувствие: гуд; Поведение: гуд");
        expected1.setExamination(ExaminationStatus.ACCEPTED);

        actual.setId(1L);
        actual.setReportDate(LocalDate.of(2023, 3, 24));
        actual.setFileId("Test124578");
        actual.setCaption("Рацион: гуд; Самочувствие: гуд; Поведение: бэд");
        actual.setExamination(ExaminationStatus.REJECTED);

        exceptionReportDog.setId(0L);
        exceptionReportDog.setReportDate(LocalDate.of(2000, 1, 1));
        exceptionReportDog.setFileId(" ");
        exceptionReportDog.setCaption(null);
        exceptionReportDog.setExamination(null);
    }

    /**
     * Тестирование метода <b>add()</b> в ReportDogController
     * <br>
     * Mockito: когда вызывается метод <b>ReportDogService::add</b>,
     * возвращается статус 200 и отчет о собаке <b>expected</b>
     * @throws Exception
     */
    @Test
    @DisplayName("Проверка получения статуса 200 и возвращения отчета о собаке " +
            "при попытке его создания и сохранения в базе данных")
    void addReportDogTest200() throws Exception {
        when(reportDogService.add(expected)).thenReturn(expected);

        mockMvc.perform(
                        post("/report_dog")
                                .content(objectMapper.writeValueAsString(expected))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }

    /**
     * Тестирование метода <b>get()</b> в ReportDogController
     * <br>
     * Mockito: когда вызывается метод <b>ReportDogService::get</b>,
     * возвращается статус 200 и отчет о собаке <b>expected</b>
     * @throws Exception
     */
    @Test
    @DisplayName("Проверка получения статуса 200 и возвращения отчета о собаке при попытке его поиска по id")
    public void getReportDogTest200() throws Exception {
        Long id = expected.getId();

        when(reportDogService.get(anyLong())).thenReturn(expected);

        mockMvc.perform(
                        get("/report_dog/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    /**
     * Тестирование метода <b>get()</b> в ReportDogController
     * <br>
     * Mockito: когда вызывается метод <b>ReportDogService::get</b>,
     * выбрасывается исключение <b>ReportDogNotFoundException</b> и
     * возвращается статус 404 <b>exceptionReportDog</b>
     * @throws Exception
     * @throws ReportDogNotFoundException
     */
    @Test
    @DisplayName("Проверка получения статуса 404 при попытке поиска по id " +
            "отчета о собаке, которого нет в базе данных")
    void getReportDogTest404() throws Exception {
        when(reportDogService.get(anyLong())).thenThrow(ReportDogNotFoundException.class);

        mockMvc.perform(
                        get("/report_dog/{id}", exceptionReportDog.getId().toString()))
                .andExpect(status().isNotFound());
    }

    /**
     * Тестирование метода <b>delete()</b> в ReportDogController
     * <br>
     * Mockito: когда вызывается метод <b>ReportDogService::remove</b>,
     * возвращается статус 200 <b>expected</b>
     * @throws Exception
     */
    @Test
    @DisplayName("Проверка получения статуса 200 при попытке удалить отчет о собаке из базы данных по id")
    public void deleteReportDogTest200() throws Exception {
        Long id = expected.getId();

        when(reportDogService.remove(anyLong())).thenReturn(true);

        mockMvc.perform(
                        delete("/report_dog/{id}", id))
                .andExpect(status().isOk());
    }

    /**
     * Тестирование метода <b>delete()</b> в ReportDogController
     * <br>
     * Mockito: когда вызывается метод <b>ReportDogService::remove</b>,
     * выбрасывается исключение <b>ReportDogNotFoundException</b> и
     * возвращается статус 404 <b>exceptionReportDog</b>
     * @throws Exception
     * @throws ReportDogNotFoundException
     */
    @Test
    @DisplayName("Проверка получения статуса 404 при попытке удалить по id " +
            "отчет о собаке, которого нет в базе данных ")
    public void deleteReportDogTest404() throws Exception {
        Long id = exceptionReportDog.getId();

        when(reportDogService.remove(anyLong())).thenThrow(ReportDogNotFoundException.class);

        mockMvc.perform(
                        delete("/report_dog/{id}", id))
                .andExpect(status().isNotFound());
    }

    /**
     * Тестирование метода <b>update()</b> в ReportDogController
     * <br>
     * Mockito: когда вызывается метод <b>ReportDogService::update</b>,
     * возвращается статус 200 и отредактированный отчет о собаке <b>expected</b>
     * @throws Exception
     */
    @Test
    @DisplayName("Проверка получения статуса 200 при попытке обновить и сохранить отчет о собаке в базе данных")
    public void updateReportDogTest200() throws Exception {
        when(reportDogService.update(expected)).thenReturn(expected);

        mockMvc.perform(
                        put("/report_dog")
                                .content(objectMapper.writeValueAsString(actual))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(actual.getId()))
                .andExpect(jsonPath("$.reportDate").value(actual.getReportDate().toString()))
                .andExpect(jsonPath("$.fileId").value(actual.getFileId()))
                .andExpect(jsonPath("$.caption").value(actual.getCaption()))
                .andExpect(jsonPath("$.examination").value(actual.getExamination().name()));
    }

    /**
     * Тестирование метода <b>update()</b> в ReportDogController
     * <br>
     * Mockito: когда вызывается метод <b>ReportDogService::update</b>,
     * выбрасывается исключение <b>ReportDogNotFoundException</b> и
     * возвращается статус 404 <b>exceptionReportDog</b>
     * @throws Exception
     * @throws ReportDogNotFoundException
     */
    @Test
    @DisplayName("Проверка получения статуса 404 при попытке обновить и сохранить " +
            "отчет о собаке, которого нет в базе данных")
    public void updateReportDogTest404() throws Exception {
        when(reportDogService.update(exceptionReportDog)).thenThrow(ReportDogNotFoundException.class);

        mockMvc.perform(
                        put("/report_dog")
                                .content(objectMapper.writeValueAsString(exceptionReportDog))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    /**
     * Тестирование метода <b>getAll()</b> в ReportDogController
     * <br>
     * Mockito: когда вызывается метод <b>ReportDogService::getAll</b>,
     * возвращается статус 200 и коллекция отчетов о собаках <b>Arrays.asList(expected, expected1)</b>
     */
    @Test
    @DisplayName("Проверка получения статуса 200 и возвращения всех отчетов о котах " +
            "при попытке их поиска в базе данных")
    void getAllReportDogsTest200() throws Exception {
        when(reportDogService.getAll()).thenReturn(Arrays.asList(expected, expected1));

        mockMvc.perform(
                        get("/report_dog/all"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(expected, expected1))));
    }
}
