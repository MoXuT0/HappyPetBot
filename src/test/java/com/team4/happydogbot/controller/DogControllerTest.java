package com.team4.happydogbot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team4.happydogbot.entity.Dog;
import com.team4.happydogbot.exception.DogNotFoundException;
import com.team4.happydogbot.service.DogService;
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
 * для класса - сервиса собак
 * @see Dog
 * @see DogService
 * @see DogController
 * @see DogControllerTest
 */
@WebMvcTest(DogController.class)
public class DogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private DogService dogService;

    private final Dog expected = new Dog();
    private final Dog expected1 = new Dog();
    private final Dog actual = new Dog();
    private final Dog exceptionDog = new Dog();

    @BeforeEach
    public void setUp() {
        expected.setId(1L);
        expected.setName("Ponchik");
        expected.setBreed("Pudel");
        expected.setYearOfBirth(2020);
        expected.setDescription("Test");

        expected1.setId(2L);
        expected1.setName("Bublik");
        expected1.setBreed("Buldog");
        expected1.setYearOfBirth(2017);
        expected1.setDescription("Test");

        actual.setId(1L);
        actual.setName("Ponchik");
        actual.setBreed("no breed");
        actual.setYearOfBirth(2019);
        actual.setDescription("Test");

        exceptionDog.setId(0L);
        exceptionDog.setName(" ");
        exceptionDog.setBreed(null);
        exceptionDog.setYearOfBirth(0);
        exceptionDog.setDescription("");
    }

    /**
     * Тестирование метода <b>add()</b> в DogController
     * <br>
     * Mockito: когда вызывается метод <b>DogService::add</b>,
     * возвращается статус 200 и собака <b>expected</b>
     * @throws Exception
     */
    @Test
    @DisplayName("Проверка получения статуса 200 и возвращения собаки при попытке ее создания и сохранения в базе данных")
    void addDogTest200() throws Exception {
        when(dogService.add(expected)).thenReturn(expected);

        mockMvc.perform(
                        post("/dog")
                                .content(objectMapper.writeValueAsString(expected))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }

    /**
     * Тестирование метода <b>get()</b> в DogController
     * <br>
     * Mockito: когда вызывается метод <b>DogService::get</b>,
     * возвращается статус 200 и собака <b>expected</b>
     * @throws Exception
     */
    @Test
    @DisplayName("Проверка получения статуса 200 и возвращения собаки при попытке ее поиска по id")
    public void getDogTest200() throws Exception {
        Long dogId = expected.getId();

        when(dogService.get(anyLong())).thenReturn(expected);

        mockMvc.perform(
                        get("/dog/{id}", dogId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dogId));
    }

    /**
     * Тестирование метода <b>get()</b> в DogController
     * <br>
     * Mockito: когда вызывается метод <b>DogService::get</b>,
     * выбрасывается исключение <b>DogNotFoundException</b> и
     * возвращается статус 404 <b>exceptionDog</b>
     * @throws Exception
     * @throws DogNotFoundException
     */
    @Test
    @DisplayName("Проверка получения статуса 404 при попытке поиска по id собаки, которой нет в базе данных")
    void getDogTest404() throws Exception {
        when(dogService.get(anyLong())).thenThrow(DogNotFoundException.class);

        mockMvc.perform(
                        get("/dog/{id}", exceptionDog.getId().toString()))
                .andExpect(status().isNotFound());
    }

    /**
     * Тестирование метода <b>delete()</b> в DogController
     * <br>
     * Mockito: когда вызывается метод <b>DogService::remove</b>,
     * возвращается статус 200 <b>expected</b>
     * @throws Exception
     */
    @Test
    @DisplayName("Проверка получения статуса 200 при попытке удалить собаку из базы данных по id")
    public void deleteDogTest200() throws Exception {
        Long dogId = expected.getId();

        when(dogService.remove(anyLong())).thenReturn(true);

        mockMvc.perform(
                        delete("/dog/{id}", dogId))
                .andExpect(status().isOk());
    }

    /**
     * Тестирование метода <b>delete()</b> в DogController
     * <br>
     * Mockito: когда вызывается метод <b>DogService::remove</b>,
     * выбрасывается исключение <b>DogNotFoundException</b> и
     * возвращается статус 404 <b>exceptionDog</b>
     * @throws Exception
     * @throws DogNotFoundException
     */
    @Test
    @DisplayName("Проверка получения статуса 404 при попытке удалить по id собаку, которой нет в базе данных ")
    public void deleteDogTest404() throws Exception {
        Long dogId = exceptionDog.getId();

        when(dogService.remove(anyLong())).thenThrow(DogNotFoundException.class);

        mockMvc.perform(
                        delete("/dog/{id}", dogId))
                .andExpect(status().isNotFound());
    }

    /**
     * Тестирование метода <b>update()</b> в DogController
     * <br>
     * Mockito: когда вызывается метод <b>DogService::update</b>,
     * возвращается статус 200 и отредактированная собака <b>expected</b>
     * @throws Exception
     */
    @Test
    @DisplayName("Проверка получения статуса 200 при попытке обновить и сохранить собаку в базе данных")
    public void updateDogTest200() throws Exception {
        when(dogService.update(expected)).thenReturn(expected);

        mockMvc.perform(
                        put("/dog")
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
     * Тестирование метода <b>update()</b> в DogController
     * <br>
     * Mockito: когда вызывается метод <b>DogService::update</b>,
     * выбрасывается исключение <b>DogNotFoundException</b> и
     * возвращается статус 404 <b>exceptionDog</b>
     * @throws Exception
     * @throws DogNotFoundException
     */
    @Test
    @DisplayName("Проверка получения статуса 404 при попытке обновить и сохранить собаку, которой нет в базе данных")
    public void updateDogTest404() throws Exception {
        when(dogService.update(exceptionDog)).thenThrow(DogNotFoundException.class);

        mockMvc.perform(
                        put("/dog")
                                .content(objectMapper.writeValueAsString(exceptionDog))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    /**
     * Тестирование метода <b>getAll()</b> в DogController
     * <br>
     * Mockito: когда вызывается метод <b>DogService::getAll</b>,
     * возвращается статус 200 и коллекция собак <b>Arrays.asList(expected, expected1)</b>
     */
    @Test
    @DisplayName("Проверка получения статуса 200 и возвращения всех собак при попытке их поиска в базе данных")
    void getAllDogsTest200() throws Exception {
        when(dogService.getAll()).thenReturn(Arrays.asList(expected, expected1));

        mockMvc.perform(
                        get("/dog/all"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(expected, expected1))));
    }
}
