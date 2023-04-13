package com.team4.happydogbot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team4.happydogbot.entity.AdopterCat;
import com.team4.happydogbot.entity.Status;
import com.team4.happydogbot.exception.AdopterCatNotFoundException;
import com.team4.happydogbot.service.AdopterCatService;
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
 * для класса - сервиса усыновителя котов
 * @see AdopterCat
 * @see AdopterCatService
 * @see AdopterCatController
 * @see AdopterCatControllerTest
 */
@WebMvcTest(AdopterCatController.class)
public class AdopterCatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    AdopterCatService adopterCatService;

    private final AdopterCat expected = new AdopterCat();
    private final AdopterCat expected1 = new AdopterCat();
    private final AdopterCat actual = new AdopterCat();
    private final AdopterCat exceptionAdopterCat = new AdopterCat();

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

        exceptionAdopterCat.setChatId(0L);
        exceptionAdopterCat.setFirstName(" ");
        exceptionAdopterCat.setLastName(null);
        exceptionAdopterCat.setUserName(null);
        exceptionAdopterCat.setAge(0);
        exceptionAdopterCat.setAddress("");
        exceptionAdopterCat.setTelephoneNumber(null);
        exceptionAdopterCat.setState(null);
    }

    /**
     * Тестирование метода <b>add()</b> в AdopterCatController
     * <br>
     * Mockito: когда вызывается метод <b>AdopterCatService::add</b>,
     * возвращается статус 200 и усыновитель кота <b>expected</b>
     * @throws Exception
     */
    @Test
    @DisplayName("Проверка получения статуса 200 и возвращения усыновителя кота " +
            "при попытке его создания и сохранения в базе данных")
    void addAdopterCatTest200() throws Exception {
        when(adopterCatService.add(expected)).thenReturn(expected);

        mockMvc.perform(
                        post("/adopter_cat")
                                .content(objectMapper.writeValueAsString(expected))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }

    /**
     * Тестирование метода <b>get()</b> в AdopterCatController
     * <br>
     * Mockito: когда вызывается метод <b>AdopterCatService::get</b>,
     * возвращается статус 200 и усыновитель кота <b>expected</b>
     * @throws Exception
     */
    @Test
    @DisplayName("Проверка получения статуса 200 и возвращения усыновителя кота при попытке его поиска по chatId")
    public void getAdopterCatTest200() throws Exception {
        Long chatId = expected.getChatId();

        when(adopterCatService.get(anyLong())).thenReturn(expected);

        mockMvc.perform(
                        get("/adopter_cat/{chatId}", chatId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chatId").value(chatId));
    }

    /**
     * Тестирование метода <b>get()</b> в AdopterCatController
     * <br>
     * Mockito: когда вызывается метод <b>AdopterCatService::get</b>,
     * выбрасывается исключение <b>AdopterCatNotFoundException</b> и
     * возвращается статус 404 <b>exceptionAdopterCat</b>
     * @throws Exception
     * @throws AdopterCatNotFoundException
     */
    @Test
    @DisplayName("Проверка получения статуса 404 при попытке поиска по chatId " +
            "усыновителя кота, которого нет в базе данных")
    void getAdopterCatTest404() throws Exception {
        when(adopterCatService.get(anyLong())).thenThrow(AdopterCatNotFoundException.class);

        mockMvc.perform(
                        get("/adopter_cat/{chatId}", exceptionAdopterCat.getChatId().toString()))
                .andExpect(status().isNotFound());
    }

    /**
     * Тестирование метода <b>delete()</b> в AdopterCatController
     * <br>
     * Mockito: когда вызывается метод <b>AdopterCatService::remove</b>,
     * возвращается статус 200 <b>expected</b>
     * @throws Exception
     */
    @Test
    @DisplayName("Проверка получения статуса 200 при попытке удалить усыновителя кота из базы данных по chatId")
    public void deleteAdopterCatTest200() throws Exception {
        Long chatId = expected.getChatId();

        when(adopterCatService.remove(anyLong())).thenReturn(true);

        mockMvc.perform(
                        delete("/adopter_cat/{chatId}", chatId))
                .andExpect(status().isOk());
    }

    /**
     * Тестирование метода <b>delete()</b> в AdopterCatController
     * <br>
     * Mockito: когда вызывается метод <b>AdopterCatService::remove</b>,
     * выбрасывается исключение <b>AdopterCatNotFoundException</b> и
     * возвращается статус 404 <b>exceptionAdopterCat</b>
     * @throws Exception
     * @throws AdopterCatNotFoundException
     */
    @Test
    @DisplayName("Проверка получения статуса 404 при попытке удалить по chatId " +
            "усыновителя кота, которого нет в базе данных ")
    public void deleteAdopterCatTest404() throws Exception {
        Long chatId = exceptionAdopterCat.getChatId();

        when(adopterCatService.remove(anyLong())).thenThrow(AdopterCatNotFoundException.class);

        mockMvc.perform(
                        delete("/adopter_cat/{chatId}", chatId))
                .andExpect(status().isNotFound());
    }

    /**
     * Тестирование метода <b>update()</b> в AdopterCatController
     * <br>
     * Mockito: когда вызывается метод <b>AdopterCatService::update</b>,
     * возвращается статус 200 и отредактированный усыновитель кота <b>expected</b>
     * @throws Exception
     */
    @Test
    @DisplayName("Проверка получения статуса 200 при попытке обновить и сохранить усыновителя кота в базе данных")
    public void updateAdopterCatTest200() throws Exception {
        when(adopterCatService.update(expected)).thenReturn(expected);

        mockMvc.perform(
                        put("/adopter_cat")
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
     * Тестирование метода <b>update()</b> в AdopterCatController
     * <br>
     * Mockito: когда вызывается метод <b>AdopterCatService::update</b>,
     * выбрасывается исключение <b>AdopterCatNotFoundException</b> и
     * возвращается статус 404 <b>exceptionAdopterCat</b>
     * @throws Exception
     * @throws AdopterCatNotFoundException
     */
    @Test
    @DisplayName("Проверка получения статуса 404 при попытке обновить и сохранить " +
            "усыновителя кота, которого нет в базе данных")
    public void updateAdopterCatTest404() throws Exception {
        when(adopterCatService.update(exceptionAdopterCat)).thenThrow(AdopterCatNotFoundException.class);

        mockMvc.perform(
                        put("/adopter_cat")
                                .content(objectMapper.writeValueAsString(exceptionAdopterCat))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    /**
     * Тестирование метода <b>getAll()</b> в AdopterCatController
     * <br>
     * Mockito: когда вызывается метод <b>AdopterCatService::getAll</b>,
     * возвращается статус 200 и коллекция усыновителей котов <b>Arrays.asList(expected, expected1)</b>
     */
    @Test
    @DisplayName("Проверка получения статуса 200 и возвращения всех усыновителей котов " +
            "при попытке их поиска в базе данных")
    void getAllAdopterCatsTest200() throws Exception {
        when(adopterCatService.getAll()).thenReturn(Arrays.asList(expected, expected1));

        mockMvc.perform(
                        get("/adopter_cat/all"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(expected, expected1))));
    }

    @Test
    @DisplayName("Проверка получения статуса 200 при отправке сообщения пользователю")
    public void sendMessageWithValidChatId() throws Exception {
        Long chatId = expected.getChatId();
        String textToSend = "Hello, world!";

        when(adopterCatService.get(chatId)).thenReturn(expected);

        mockMvc.perform(get("/adopter_cat/send_message")
                        .param("chatId", String.valueOf(chatId))
                        .param("textToSend", textToSend))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Проверка получения статуса 404 при отправке сообщения пользователю которого не существует")
    public void sendMessageWithInvalidChatId() throws Exception {
        Long chatId = anyLong();
        String textToSend = "Hello, world!";
        when(adopterCatService.get(chatId)).thenReturn(null);

        mockMvc.perform(get("/adopter_cat/send_message")
                        .param("chatId", String.valueOf(chatId))
                        .param("textToSend", textToSend))
                .andExpect(status().isNotFound());
    }
}
