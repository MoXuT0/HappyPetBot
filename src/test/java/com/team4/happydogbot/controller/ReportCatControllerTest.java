package com.team4.happydogbot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team4.happydogbot.entity.ExaminationStatus;
import com.team4.happydogbot.entity.ReportCat;
import com.team4.happydogbot.exception.ReportCatNotFoundException;
import com.team4.happydogbot.service.ReportCatService;
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
 * для класса - сервиса отчетов о котах
 * @see ReportCat
 * @see ReportCatService
 * @see ReportCatController
 * @see ReportCatControllerTest
 */
@WebMvcTest(ReportCatController.class)
public class ReportCatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ReportCatService reportCatService;

    private final ReportCat expected = new ReportCat();
    private final ReportCat expected1 = new ReportCat();
    private final ReportCat actual = new ReportCat();
    private final ReportCat exceptionReportCat = new ReportCat();

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
        actual.setExamination(ExaminationStatus.UNCHECKED);

        exceptionReportCat.setId(0L);
        exceptionReportCat.setReportDate(LocalDate.of(2000, 1, 1));
        exceptionReportCat.setFileId(" ");
        exceptionReportCat.setCaption(null);
        exceptionReportCat.setExamination(null);
    }

    /**
     * Тестирование метода <b>add()</b> в ReportCatController
     * <br>
     * Mockito: когда вызывается метод <b>ReportCatService::add</b>,
     * возвращается статус 200 и отчет о коте <b>expected</b>
     * @throws Exception
     */
    @Test
    @DisplayName("Проверка получения статуса 200 и возвращения отчета о коте " +
            "при попытке его создания и сохранения в базе данных")
    void addReportCatTest200() throws Exception {
        when(reportCatService.add(expected)).thenReturn(expected);

        mockMvc.perform(
                        post("/report_cat")
                                .content(objectMapper.writeValueAsString(expected))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }

    /**
     * Тестирование метода <b>get()</b> в ReportCatController
     * <br>
     * Mockito: когда вызывается метод <b>ReportCatService::get</b>,
     * возвращается статус 200 и отчет о коте <b>expected</b>
     * @throws Exception
     */
    @Test
    @DisplayName("Проверка получения статуса 200 и возвращения отчета о коте при попытке его поиска по id")
    public void getReportCatTest200() throws Exception {
        Long id = expected.getId();

        when(reportCatService.get(anyLong())).thenReturn(expected);

        mockMvc.perform(
                        get("/report_cat/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    /**
     * Тестирование метода <b>get()</b> в ReportCatController
     * <br>
     * Mockito: когда вызывается метод <b>ReportCatService::get</b>,
     * выбрасывается исключение <b>ReportCatNotFoundException</b> и
     * возвращается статус 404 <b>exceptionReportCat</b>
     * @throws Exception
     * @throws ReportCatNotFoundException
     */
    @Test
    @DisplayName("Проверка получения статуса 404 при попытке поиска по id " +
            "отчета о коте, которого нет в базе данных")
    void getReportCatTest404() throws Exception {
        when(reportCatService.get(anyLong())).thenThrow(ReportCatNotFoundException.class);

        mockMvc.perform(
                        get("/report_cat/{id}", exceptionReportCat.getId().toString()))
                .andExpect(status().isNotFound());
    }

    /**
     * Тестирование метода <b>delete()</b> в ReportCatController
     * <br>
     * Mockito: когда вызывается метод <b>ReportCatService::remove</b>,
     * возвращается статус 200 <b>expected</b>
     * @throws Exception
     */
    @Test
    @DisplayName("Проверка получения статуса 200 при попытке удалить отчет о коте из базы данных по id")
    public void deleteReportCatTest200() throws Exception {
        Long id = expected.getId();

        when(reportCatService.remove(anyLong())).thenReturn(true);

        mockMvc.perform(
                        delete("/report_cat/{id}", id))
                .andExpect(status().isOk());
    }

    /**
     * Тестирование метода <b>delete()</b> в ReportCatController
     * <br>
     * Mockito: когда вызывается метод <b>ReportCatService::remove</b>,
     * выбрасывается исключение <b>ReportCatNotFoundException</b> и
     * возвращается статус 404 <b>exceptionReportCat</b>
     * @throws Exception
     * @throws ReportCatNotFoundException
     */
    @Test
    @DisplayName("Проверка получения статуса 404 при попытке удалить по id " +
            "отчет о коте, которого нет в базе данных ")
    public void deleteReportCatTest404() throws Exception {
        Long id = exceptionReportCat.getId();

        when(reportCatService.remove(anyLong())).thenThrow(ReportCatNotFoundException.class);

        mockMvc.perform(
                        delete("/report_cat/{id}", id))
                .andExpect(status().isNotFound());
    }

    /**
     * Тестирование метода <b>update()</b> в ReportCatController
     * <br>
     * Mockito: когда вызывается метод <b>ReportCatService::update</b>,
     * возвращается статус 200 и отредактированный отчет о коте <b>expected</b>
     * @throws Exception
     */
    @Test
    @DisplayName("Проверка получения статуса 200 при попытке обновить и сохранить отчет о коте в базе данных")
    public void updateReportCatTest200() throws Exception {
        when(reportCatService.update(expected)).thenReturn(expected);

        mockMvc.perform(
                        put("/report_cat")
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
     * Тестирование метода <b>update()</b> в ReportCatController
     * <br>
     * Mockito: когда вызывается метод <b>ReportCatService::update</b>,
     * выбрасывается исключение <b>ReportCatNotFoundException</b> и
     * возвращается статус 404 <b>exceptionReportCat</b>
     * @throws Exception
     * @throws ReportCatNotFoundException
     */
    @Test
    @DisplayName("Проверка получения статуса 404 при попытке обновить и сохранить " +
            "отчет о коте, которого нет в базе данных")
    public void updateReportCatTest404() throws Exception {
        when(reportCatService.update(exceptionReportCat)).thenThrow(ReportCatNotFoundException.class);

        mockMvc.perform(
                        put("/report_cat")
                                .content(objectMapper.writeValueAsString(exceptionReportCat))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    /**
     * Тестирование метода <b>getAll()</b> в ReportCatController
     * <br>
     * Mockito: когда вызывается метод <b>ReportCatService::getAll</b>,
     * возвращается статус 200 и коллекция отчетов о котах <b>Arrays.asList(expected, expected1)</b>
     */
    @Test
    @DisplayName("Проверка получения статуса 200 и возвращения всех отчетов о котах " +
            "при попытке их поиска в базе данных")
    void getAllReportCatsTest200() throws Exception {
        when(reportCatService.getAll()).thenReturn(Arrays.asList(expected, expected1));

        mockMvc.perform(
                        get("/report_cat/all"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(expected, expected1))));
    }
}
