package com.team4.happydogbot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team4.happydogbot.entity.Cat;
import com.team4.happydogbot.exception.CatNotFoundException;
import com.team4.happydogbot.service.CatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Тест - класс для проверки API endpoints при обращении к маршрутам отдельными HTTP методами
 * для класса - сервиса котов
 * @see Cat
 * @see CatService
 * @see CatController
 * @see CatControllerTest
 */
@WebMvcTest(CatController.class)
public class CatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private CatService catService;

    private final Cat expected = new Cat();
    private final Cat expected1 = new Cat();
    private final Cat actual = new Cat();
    private final Cat exceptionCat = new Cat();

    @BeforeEach
    public void setUp() {
        expected.setId(1L);
        expected.setName("Ponchik");
        expected.setBreed("Bolinez");
        expected.setYearOfBirth(2020);
        expected.setDescription("Test");

        expected1.setId(2L);
        expected1.setName("Bublik");
        expected1.setBreed("Siam");
        expected1.setYearOfBirth(2017);
        expected1.setDescription("Test");

        actual.setId(1L);
        actual.setName("Ponchik");
        actual.setBreed("no breed");
        actual.setYearOfBirth(2020);
        actual.setDescription("Test");

        exceptionCat.setId(0L);
        exceptionCat.setName(" ");
        exceptionCat.setBreed(null);
        exceptionCat.setYearOfBirth(0);
        exceptionCat.setDescription("");
    }

    /**
     * Тестирование метода <b>add()</b> в CatController
     * <br>
     * Mockito: когда вызывается метод <b>CatService::add</b>,
     * возвращается статус 200 и кот <b>expected</b>
     * @throws Exception
     */
    @Test
    @DisplayName("Проверка получения статуса 200 и возвращения кота при попытке его создания и сохранения в базе данных")
    void addCatTest200() throws Exception {
        when(catService.add(expected)).thenReturn(expected);

        mockMvc.perform(
                        post("/cat")
                                .content(objectMapper.writeValueAsString(expected))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }

    /**
     * Тестирование метода <b>get()</b> в CatController
     * <br>
     * Mockito: когда вызывается метод <b>CatService::get</b>,
     * возвращается статус 200 и кот <b>expected</b>
     * @throws Exception
     */
    @Test
    @DisplayName("Проверка получения статуса 200 и возвращения кота при попытке его поиска по id")
    public void getCatTest200() throws Exception {
        Long catId = expected.getId();

        when(catService.get(anyLong())).thenReturn(expected);

        mockMvc.perform(
                        get("/cat/{id}", catId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(catId));
    }

    /**
     * Тестирование метода <b>get()</b> в CatController
     * <br>
     * Mockito: когда вызывается метод <b>CatService::get</b>,
     * выбрасывается исключение <b>CatNotFoundException</b> и
     * возвращается статус 404 <b>exceptionCat</b>
     * @throws Exception
     * @throws CatNotFoundException
     */
    @Test
    @DisplayName("Проверка получения статуса 404 при попытке поиска по id кота, которого нет в базе данных")
    void getCatTest404() throws Exception {
        when(catService.get(anyLong())).thenThrow(CatNotFoundException.class);

        mockMvc.perform(
                get("/cat/{id}", exceptionCat.getId().toString()))
                .andExpect(status().isNotFound());
    }

    /**
     * Тестирование метода <b>delete()</b> в CatController
     * <br>
     * Mockito: когда вызывается метод <b>CatService::remove</b>,
     * возвращается статус 200 <b>expected</b>
     * @throws Exception
     */
    @Test
    @DisplayName("Проверка получения статуса 200 при попытке удалить кота из базы данных по id")
    public void deleteCatTest200() throws Exception {
        Long catId = expected.getId();

        when(catService.remove(anyLong())).thenReturn(true);

        mockMvc.perform(
                        delete("/cat/{id}", catId))
                .andExpect(status().isOk());
    }

    /**
     * Тестирование метода <b>delete()</b> в CatController
     * <br>
     * Mockito: когда вызывается метод <b>CatService::remove</b>,
     * выбрасывается исключение <b>CatNotFoundException</b> и
     * возвращается статус 404 <b>exceptionCat</b>
     * @throws Exception
     * @throws CatNotFoundException
     */
    @Test
    @DisplayName("Проверка получения статуса 404 при попытке удалить по id кота, которого нет в базе данных ")
    public void deleteCatTest404() throws Exception {
        Long catId = exceptionCat.getId();

        when(catService.remove(anyLong())).thenThrow(CatNotFoundException.class);

        mockMvc.perform(
                        delete("/cat/{id}", catId))
                .andExpect(status().isNotFound());
    }

    /**
     * Тестирование метода <b>update()</b> в CatController
     * <br>
     * Mockito: когда вызывается метод <b>CatService::update</b>,
     * возвращается статус 200 и отредактированный кот <b>expected</b>
     * @throws Exception
     */
    @Test
    @DisplayName("Проверка получения статуса 200 при попытке обновить и сохранить кота в базе данных")
    public void updateCatTest200() throws Exception {
        when(catService.update(expected)).thenReturn(expected);

        mockMvc.perform(
                        put("/cat")
                                .content(objectMapper.writeValueAsString(actual))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(actual.getId()))
                .andExpect(jsonPath("$.name").value(actual.getName()))
                .andExpect(jsonPath("$.breed").value(actual.getBreed()))
                .andExpect(jsonPath("$.yearOfBirth").value(actual.getYearOfBirth()))
                .andExpect(jsonPath("$.description").value(actual.getDescription()));
    }

    /**
     * Тестирование метода <b>update()</b> в CatController
     * <br>
     * Mockito: когда вызывается метод <b>CatService::update</b>,
     * выбрасывается исключение <b>CatNotFoundException</b> и
     * возвращается статус 404 <b>exceptionCat</b>
     * @throws Exception
     * @throws CatNotFoundException
     */
    @Test
    @DisplayName("Проверка получения статуса 404 при попытке обновить и сохранить кота, которого нет в базе данных")
    public void updateCatTest404() throws Exception {
        when(catService.update(exceptionCat)).thenThrow(CatNotFoundException.class);

        mockMvc.perform(
                put("/cat")
                        .content(objectMapper.writeValueAsString(exceptionCat))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    /**
     * Тестирование метода <b>getAll()</b> в CatController
     * <br>
     * Mockito: когда вызывается метод <b>CatService::getAll</b>,
     * возвращается статус 200 и коллекция котов <b>Arrays.asList(expected, expected1)</b>
     */
    @Test
    @DisplayName("Проверка получения статуса 200 и возвращения всех котов при попытке их поиска в базе данных")
    void getAllCatsTest200() throws Exception {
        when(catService.getAll()).thenReturn(Arrays.asList(expected, expected1));

        mockMvc.perform(
                        get("/cat/all"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(expected, expected1))));
    }
}
