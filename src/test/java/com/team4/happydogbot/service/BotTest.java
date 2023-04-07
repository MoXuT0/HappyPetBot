package com.team4.happydogbot.service;

import com.team4.happydogbot.config.BotConfig;
import com.team4.happydogbot.entity.AdopterCat;
import com.team4.happydogbot.entity.AdopterDog;
import com.team4.happydogbot.entity.Status;
import com.team4.happydogbot.repository.AdopterCatRepository;
import com.team4.happydogbot.repository.AdopterDogRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static com.team4.happydogbot.constants.BotCommands.KEYBOARD_DECISION;
import static com.team4.happydogbot.constants.BotCommands.TAKE_DECISION;
import static com.team4.happydogbot.constants.BotReplies.MESSAGE_DECISION_EXTEND_14;
import static com.team4.happydogbot.entity.Status.ADDITIONAL_PERIOD_14;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;


/**
 * Тест - класс для проверки операций в классе - сервисе Bot
 *
 * @see BotConfig
 * @see AdopterDogService
 * @see AdopterDogRepository
 * @see AdopterCatService
 * @see AdopterCatRepository
 */
@ExtendWith(MockitoExtension.class)
public class BotTest {
    @Mock
    private BotConfig botConfig;
    @Mock
    private AdopterCatRepository adopterCatRepository;
    @Mock
    private AdopterDogRepository adopterDogRepository;
    @Mock
    private AdopterCatService adopterCatService;
    @Mock
    private AdopterDogService adopterDogService;

    @Spy
    @InjectMocks
    private Bot bot;

    /**
     * Тестирование метода <b>sendFinishListForDogVolunteer()</b> в Bot<br>
     * Mockito: когда вызывается метод <b>AdopterDogRepository::findAll</b>,
     * возвращается список содержащий 1 усыновителя собаки <b>expected</b>
     * со статусом PROBATION и датой изменеия статуса 31 день назад от текущей даты.<br>
     * Кол-во вызовов метода <b>sendMessageWithInlineKeyboard</b> равно 1.
     */

    @Test
    @DisplayName("Проверка отправки по расписанию при наличии собачьих усыновителей с необходимым статусом")
    public void testSendFinishListForDogVolunteer() throws Exception {
        AdopterDog expected = new AdopterDog();

        expected.setChatId(1234567890L);
        expected.setFirstName("Ivan");
        expected.setLastName("Ivanov");
        expected.setUserName("iiivanov");
        expected.setAge(33);
        expected.setAddress("МСК...");
        expected.setTelephoneNumber("7951...");
        expected.setStatusDate(LocalDate.now().minusDays(31));
        expected.setState(Status.PROBATION);


        when(adopterDogRepository.findAll()).thenReturn(List.of(expected));
        bot.sendFinishListForDogVolunteer();

        Thread.sleep(500);

        Mockito.verify(bot, Mockito.times(1))
                .sendMessageWithInlineKeyboard(0L,
                        TAKE_DECISION + expected.getUserName(),
                        KEYBOARD_DECISION);
    }

    /**
     * Тестирование метода <b>sendFinishListForDogVolunteer()</b> в Bot<br>
     * Mockito: когда вызывается метод <b>AdopterDogRepository::findAll</b>,
     * возвращается список содержащий 1 усыновителя собаки <b>expected</b>
     * со статусом REGISTRATION и датой изменеия статуса 31 день назад от текущей даты.<br>
     * Кол-во вызовов метода <b>sendMessageWithInlineKeyboard</b> равно 0.
     */
    @Test
    @DisplayName("Проверка неотправки по расписанию при отсутствии собачьих усыновителей с необходимым статусом")
    public void testSendFinishListForDogVolunteerWithoutAdopter() throws Exception {
        AdopterDog expected = new AdopterDog();

        expected.setChatId(1234567890L);
        expected.setFirstName("Ivan");
        expected.setLastName("Ivanov");
        expected.setUserName("iiivanov");
        expected.setAge(33);
        expected.setAddress("МСК...");
        expected.setTelephoneNumber("7951...");
        expected.setStatusDate(LocalDate.now().minusDays(31));
        expected.setState(Status.REGISTRATION);


        when(adopterDogRepository.findAll()).thenReturn(List.of(expected));
        bot.sendFinishListForDogVolunteer();

        Thread.sleep(500);

        Mockito.verify(bot, Mockito.times(0))
                .sendMessageWithInlineKeyboard(0L,
                        TAKE_DECISION + expected.getUserName(),
                        KEYBOARD_DECISION);
    }

    /**
     * Тестирование метода <b>sendFinishListForCatVolunteer()</b> в Bot<br>
     * Mockito: когда вызывается метод <b>AdopterCatRepository::findAll</b>,
     * возвращается список содержащий 1 усыновителя собаки <b>expected</b>
     * со статусом PROBATION и датой изменеия статуса 31 день назад от текущей даты.<br>
     * Кол-во вызовов метода <b>sendMessageWithInlineKeyboard</b> равно 1.
     */

    @Test
    @DisplayName("Проверка отправки по расписанию при наличии кошачих усыновителей с необходимым статусом")
    public void testSendFinishListForCatVolunteer() throws Exception {
        AdopterCat expected = new AdopterCat();

        expected.setChatId(1234567890L);
        expected.setFirstName("Ivan");
        expected.setLastName("Ivanov");
        expected.setUserName("iiivanov");
        expected.setAge(33);
        expected.setAddress("МСК...");
        expected.setTelephoneNumber("7951...");
        expected.setStatusDate(LocalDate.now().minusDays(31));
        expected.setState(Status.PROBATION);

        when(adopterCatRepository.findAll()).thenReturn(List.of(expected));
        bot.sendFinishListForCatVolunteer();

        Thread.sleep(500);

        Mockito.verify(bot, Mockito.times(1))
                .sendMessageWithInlineKeyboard(0L,
                        TAKE_DECISION + expected.getUserName(),
                        KEYBOARD_DECISION);
    }

    /**
     * Тестирование метода <b>sendFinishListForCatVolunteer()</b> в Bot<br>
     * Mockito: когда вызывается метод <b>AdopterCatRepository::findAll</b>,
     * возвращается список содержащий 1 усыновителя собаки <b>expected</b>
     * со статусом REGISTRATION и датой изменеия статуса 31 день назад от текущей даты.<br>
     * Кол-во вызовов метода <b>sendMessageWithInlineKeyboard</b> равно 0.
     */
    @Test
    @DisplayName("Проверка неотправки по расписанию при отсутствии кошачих усыновителей с необходимым статусом")
    public void testSendFinishListForCatVolunteerWithoutAdopter() throws Exception {
        AdopterCat expected = new AdopterCat();

        expected.setChatId(1234567890L);
        expected.setFirstName("Ivan");
        expected.setLastName("Ivanov");
        expected.setUserName("iiivanov");
        expected.setAge(33);
        expected.setAddress("МСК...");
        expected.setTelephoneNumber("7951...");
        expected.setStatusDate(LocalDate.now().minusDays(31));
        expected.setState(Status.REGISTRATION);

        when(adopterCatRepository.findAll()).thenReturn(List.of(expected));
        bot.sendFinishListForCatVolunteer();

        Thread.sleep(500);

        Mockito.verify(bot, Mockito.times(0))
                .sendMessageWithInlineKeyboard(0L,
                        TAKE_DECISION + expected.getUserName(),
                        KEYBOARD_DECISION);
    }

    /**
     * Тестирование метода <b>changeDogAdopterStatus()</b> в Bot<br>
     * Mockito: <br>
     * - когда вызывается метод <b>AdopterDogRepository::findAll</b>;<br>
     * возвращается список содержащий 1 усыновителя собаки <b>expected</b>
     * со статусом <b>REGISTRATION</b>;<br>
     * - когда вызывается метод <b>AdopterDogService::get</b>,
     * возвращает по id 1 усыновителя собаки <b>expected</b><br>
     * - проверка <b>assertThat</b> успешного иземенения статуса усыновителя на <b>ADDITIONAL_PERIOD_14</b>;<br>
     * - кол-во вызовов метода <b>sendMessage</b> равно 2.
     */

    @Test
    @DisplayName("Проверка смены статуса собачьего усыновителя")
    public void testChangeDogAdopterStatus() throws Exception {
        String messageText = "dfgs: iiivanov";
        AdopterDog expected = new AdopterDog();

        expected.setChatId(1234567890L);
        expected.setFirstName("Ivan");
        expected.setLastName("Ivanov");
        expected.setUserName("iiivanov");
        expected.setAge(33);
        expected.setAddress("МСК...");
        expected.setTelephoneNumber("7951...");
        expected.setState(Status.REGISTRATION);

        when(adopterDogRepository.findAll()).thenReturn(List.of(expected));

        when(adopterDogService.get(1234567890L)).thenReturn(expected);

        bot.changeDogAdopterStatus(MESSAGE_DECISION_EXTEND_14, messageText, ADDITIONAL_PERIOD_14);

        Assertions.assertThat(expected.getState().compareTo(ADDITIONAL_PERIOD_14));
        Mockito.verify(bot, Mockito.times(2))
                .sendMessage(anyLong(),any());
    }

    /**
     * Тестирование метода <b>changeDogAdopterStatus()</b> в Bot<br>
     * Mockito: <br>
     * - когда вызывается метод <b>AdopterDogRepository::findAll</b>;<br>
     * возвращается список содержащий 1 усыновителя собаки <b>expected</b>
     * со статусом <b>REGISTRATION</b>;<br>
     * - когда вызывается метод <b>AdopterDogService::get</b>,
     * возвращает по id 1 усыновителя собаки <b>expected</b><br>
     * - проверка <b>assertThat</b> успешного иземенения статуса усыновителя на <b>ADDITIONAL_PERIOD_14</b>;<br>
     * - кол-во вызовов метода <b>sendMessage</b> равно 2.
     */

    @Test
    @DisplayName("Проверка смены статуса кошачьего усыновителя")
    public void testChangeCatAdopterStatus() throws Exception {
        String messageText = "dfgs: iiivanov";
        AdopterCat expected = new AdopterCat();

        expected.setChatId(1234567890L);
        expected.setFirstName("Ivan");
        expected.setLastName("Ivanov");
        expected.setUserName("iiivanov");
        expected.setAge(33);
        expected.setAddress("МСК...");
        expected.setTelephoneNumber("7951...");
        expected.setState(Status.REGISTRATION);

        when(adopterCatRepository.findAll()).thenReturn(List.of(expected));

        when(adopterCatService.get(1234567890L)).thenReturn(expected);

        bot.changeCatAdopterStatus(MESSAGE_DECISION_EXTEND_14, messageText, ADDITIONAL_PERIOD_14);

        Assertions.assertThat(expected.getState().compareTo(ADDITIONAL_PERIOD_14));
        Mockito.verify(bot, Mockito.times(2))
                .sendMessage(anyLong(),any());
    }
}
