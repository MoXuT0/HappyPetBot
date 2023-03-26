package com.team4.happydogbot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team4.happydogbot.entity.AdopterDog;
import com.team4.happydogbot.entity.Status;
import com.team4.happydogbot.exception.AdopterDogNotFoundException;
import com.team4.happydogbot.service.AdopterDogService;
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
 * для класса - сервиса усыновителей собак
 * @see AdopterDog
 * @see AdopterDogService
 * @see AdopterDogController
 * @see AdopterDogControllerTest
 */
@WebMvcTest(AdopterDogController.class)
public class AdopterDogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    AdopterDogService adopterDogService;

    private final AdopterDog expected = new AdopterDog();
    private final AdopterDog expected1 = new AdopterDog();
    private final AdopterDog actual = new AdopterDog();
    private final AdopterDog exceptionAdopterDog = new AdopterDog();

    @BeforeEach
    public void setUp() {
        expected.setChatId(1234567890L);
        expected.setFirstName("Ivan");
        expected.setLastName("Ivanov");
        expected.setUserName("iiivanov");
        expected.setAge(33);
        expected.setAddress("МСК...");
        expected.setTelephoneNumber("7951...");
        expected.setState(Status.REGISTRATION);

        expected1.setChatId(9876543210L);
        expected1.setFirstName("Petr");
        expected1.setLastName("Petrov");
        expected1.setUserName("pppetrov");
        expected1.setAge(23);
        expected1.setAddress("МСК...");
        expected1.setTelephoneNumber("7902...");
        expected1.setState(Status.REGISTRATION);

        actual.setChatId(1234567890L);
        actual.setFirstName("Ivan");
        actual.setLastName("Ivanov");
        actual.setUserName("iiivanov");
        actual.setAge(33);
        actual.setAddress("КРАСНОДАР...");
        actual.setTelephoneNumber("7964...");
        actual.setState(Status.PROBATION);

        exceptionAdopterDog.setChatId(0L);
        exceptionAdopterDog.setFirstName(" ");
        exceptionAdopterDog.setLastName(null);
        exceptionAdopterDog.setUserName(null);
        exceptionAdopterDog.setAge(0);
        exceptionAdopterDog.setAddress("");
        exceptionAdopterDog.setTelephoneNumber(null);
        exceptionAdopterDog.setState(null);
    }

    /**
     * Тестирование метода <b>add()</b> в AdopterDogController
     * <br>
     * Mockito: когда вызывается метод <b>AdopterDogService::add</b>,
     * возвращается статус 200 и усыновитель собаки <b>expected</b>
     * @throws Exception
     */
    @Test
    @DisplayName("Проверка получения статуса 200 и возвращения усыновителя собаки " +
            "при попытке его создания и сохранения в базе данных")
    void addAdopterDogTest200() throws Exception {
        when(adopterDogService.add(expected)).thenReturn(expected);

        mockMvc.perform(
                        post("/adopter_dog")
                                .content(objectMapper.writeValueAsString(expected))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }

    /**
     * Тестирование метода <b>get()</b> в AdopterDogController
     * <br>
     * Mockito: когда вызывается метод <b>AdopterDogService::get</b>,
     * возвращается статус 200 и усыновитель собаки <b>expected</b>
     * @throws Exception
     */
    @Test
    @DisplayName("Проверка получения статуса 200 и возвращения усыновителя собаки при попытке его поиска по chatId")
    public void getAdopterDogTest200() throws Exception {
        Long chatId = expected.getChatId();

        when(adopterDogService.get(anyLong())).thenReturn(expected);

        mockMvc.perform(
                        get("/adopter_dog/{chatId}", chatId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chatId").value(chatId));
    }

    /**
     * Тестирование метода <b>get()</b> в AdopterDogController
     * <br>
     * Mockito: когда вызывается метод <b>AdopterDogService::get</b>,
     * выбрасывается исключение <b>AdopterDogNotFoundException</b> и
     * возвращается статус 404 <b>exceptionAdopterDog</b>
     * @throws Exception
     * @throws AdopterDogNotFoundException
     */
    @Test
    @DisplayName("Проверка получения статуса 404 при попытке поиска по chatId " +
            "усыновителя собаки, которого нет в базе данных")
    void getAdopterDogTest404() throws Exception {
        when(adopterDogService.get(anyLong())).thenThrow(AdopterDogNotFoundException.class);

        mockMvc.perform(
                        get("/adopter_dog/{chatId}", exceptionAdopterDog.getChatId().toString()))
                .andExpect(status().isNotFound());
    }

    /**
     * Тестирование метода <b>delete()</b> в AdopterDogController
     * <br>
     * Mockito: когда вызывается метод <b>AdopterDogService::remove</b>,
     * возвращается статус 200 <b>expected</b>
     * @throws Exception
     */
    @Test
    @DisplayName("Проверка получения статуса 200 при попытке удалить усыновителя собаки из базы данных по chatId")
    public void deleteAdopterDogTest200() throws Exception {
        Long chatId = expected.getChatId();

        when(adopterDogService.remove(anyLong())).thenReturn(true);

        mockMvc.perform(
                        delete("/adopter_dog/{chatId}", chatId))
                .andExpect(status().isOk());
    }

    /**
     * Тестирование метода <b>delete()</b> в AdopterDogController
     * <br>
     * Mockito: когда вызывается метод <b>AdopterDogService::remove</b>,
     * выбрасывается исключение <b>AdopterDogNotFoundException</b> и
     * возвращается статус 404 <b>exceptionAdopterDog</b>
     * @throws Exception
     * @throws AdopterDogNotFoundException
     */
    @Test
    @DisplayName("Проверка получения статуса 404 при попытке удалить по chatId " +
            "усыновителя собаки, которого нет в базе данных ")
    public void deleteAdopterDogTest404() throws Exception {
        Long chatId = exceptionAdopterDog.getChatId();

        when(adopterDogService.remove(anyLong())).thenThrow(AdopterDogNotFoundException.class);

        mockMvc.perform(
                        delete("/adopter_dog/{chatId}", chatId))
                .andExpect(status().isNotFound());
    }

    /**
     * Тестирование метода <b>update()</b> в AdopterDogController
     * <br>
     * Mockito: когда вызывается метод <b>AdopterDogService::update</b>,
     * возвращается статус 200 и отредактированный усыновитель собаки <b>expected</b>
     * @throws Exception
     */
    @Test
    @DisplayName("Проверка получения статуса 200 при попытке обновить и сохранить усыновителя собаки в базе данных")
    public void updateAdopterDogTest200() throws Exception {
        when(adopterDogService.update(expected)).thenReturn(expected);

        mockMvc.perform(
                        put("/adopter_dog")
                                .content(objectMapper.writeValueAsString(actual))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chatId").value(actual.getChatId()))
                .andExpect(jsonPath("$.firstName").value(actual.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(actual.getLastName()))
                .andExpect(jsonPath("$.userName").value(actual.getUserName()))
                .andExpect(jsonPath("$.age").value(actual.getAge()))
                .andExpect(jsonPath("$.address").value(actual.getAddress()))
                .andExpect(jsonPath("$.telephoneNumber").value(actual.getTelephoneNumber()))
                .andExpect(jsonPath("$.state").value(actual.getState().name()));
    }

    /**
     * Тестирование метода <b>update()</b> в AdopterDogController
     * <br>
     * Mockito: когда вызывается метод <b>AdopterDogService::update</b>,
     * выбрасывается исключение <b>AdopterDogNotFoundException</b> и
     * возвращается статус 404 <b>exceptionAdopterDog</b>
     * @throws Exception
     * @throws AdopterDogNotFoundException
     */
    @Test
    @DisplayName("Проверка получения статуса 404 при попытке обновить и сохранить " +
            "усыновителя собаки, которого нет в базе данных")
    public void updateAdopterDogTest404() throws Exception {
        when(adopterDogService.update(exceptionAdopterDog)).thenThrow(AdopterDogNotFoundException.class);

        mockMvc.perform(
                        put("/adopter_dog")
                                .content(objectMapper.writeValueAsString(exceptionAdopterDog))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    /**
     * Тестирование метода <b>getAll()</b> в AdopterDogController
     * <br>
     * Mockito: когда вызывается метод <b>AdopterDogService::getAll</b>,
     * возвращается статус 200 и коллекция усыновителей собак <b>Arrays.asList(expected, expected1)</b>
     */
    @Test
    @DisplayName("Проверка получения статуса 200 и возвращения всех усыновителей собак " +
            "при попытке их поиска в базе данных")
    void getAllAdopterDogsTest200() throws Exception {
        when(adopterDogService.getAll()).thenReturn(Arrays.asList(expected, expected1));

        mockMvc.perform(
                        get("/adopter_dog/all"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(expected, expected1))));
    }
}
