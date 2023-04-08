package com.team4.happydogbot.service;

import com.team4.happydogbot.config.BotConfig;
import com.team4.happydogbot.entity.*;
import com.team4.happydogbot.repository.AdopterCatRepository;
import com.team4.happydogbot.repository.AdopterDogRepository;
import com.team4.happydogbot.repository.ReportCatRepository;
import com.team4.happydogbot.repository.ReportDogRepository;
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

import static com.team4.happydogbot.constants.BotCommands.*;
import static com.team4.happydogbot.constants.BotReplies.*;
import static com.team4.happydogbot.entity.Status.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


/**
 * Тест - класс для проверки операций в классе - сервисе Bot
 *
 * @see BotConfig
 * @see AdopterDogService
 * @see AdopterDogRepository
 * @see ReportDogRepository
 * @see AdopterCatService
 * @see AdopterCatRepository
 * @see ReportCatRepository
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
    private ReportDogRepository reportDogRepository;
    @Mock
    private AdopterCatService adopterCatService;
    @Mock
    private AdopterDogService adopterDogService;
    @Mock
    private ReportCatRepository reportCatRepository;

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
        String messageText = "text: iiivanov";
        AdopterDog expected = new AdopterDog();

        expected.setChatId(1L);
        expected.setFirstName("Ivan");
        expected.setLastName("Ivanov");
        expected.setUserName("iiivanov");
        expected.setAge(33);
        expected.setAddress("МСК...");
        expected.setTelephoneNumber("7951...");
        expected.setState(Status.REGISTRATION);

        when(adopterDogRepository.findAll()).thenReturn(List.of(expected));

        when(adopterDogService.get(1L)).thenReturn(expected);

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
        String messageText = "text: iiivanov";
        AdopterCat expected = new AdopterCat();

        expected.setChatId(1L);
        expected.setFirstName("Ivan");
        expected.setLastName("Ivanov");
        expected.setUserName("iiivanov");
        expected.setAge(33);
        expected.setAddress("МСК...");
        expected.setTelephoneNumber("7951...");
        expected.setState(Status.REGISTRATION);

        when(adopterCatRepository.findAll()).thenReturn(List.of(expected));

        when(adopterCatService.get(1L)).thenReturn(expected);

        bot.changeCatAdopterStatus(MESSAGE_DECISION_EXTEND_14, messageText, ADDITIONAL_PERIOD_14);

        Assertions.assertThat(expected.getState().compareTo(ADDITIONAL_PERIOD_14));
        Mockito.verify(bot, Mockito.times(2))
                .sendMessage(anyLong(),any());
    }

    @Test
    @DisplayName("Проверка отправки уведомления для волонтера и собачьего усыновителя")
    public void testSendAttentionForDogVolunteerAndAdopterDog() throws Exception {
        AdopterDog expected1 = new AdopterDog();
        AdopterDog expected2 = new AdopterDog();
        AdopterDog expected3 = new AdopterDog();

        ReportDog report1 = new ReportDog();
        ReportDog report2 = new ReportDog();
        ReportDog report3 = new ReportDog();

        expected1.setChatId(1L);
        expected1.setFirstName("Ivan");
        expected1.setLastName("Ivanov");
        expected1.setUserName("iiivanov");
        expected1.setAge(33);
        expected1.setAddress("МСК...");
        expected1.setTelephoneNumber("7951...");
        expected1.setState(PROBATION);
        expected1.setReports(List.of(report1));

        expected2.setChatId(2L);
        expected2.setFirstName("Petr");
        expected2.setLastName("Petrov");
        expected2.setUserName("pppetrov");
        expected2.setAge(33);
        expected2.setAddress("МСК...");
        expected2.setTelephoneNumber("7951...");
        expected2.setState(ADDITIONAL_PERIOD_14);
        expected1.setReports(List.of(report2));

        expected3.setChatId(3L);
        expected3.setFirstName("Denis");
        expected3.setLastName("Denisov");
        expected3.setUserName("dddenisov");
        expected3.setAge(33);
        expected3.setAddress("МСК...");
        expected3.setTelephoneNumber("7951...");
        expected3.setState(ADDITIONAL_PERIOD_30);
        expected1.setReports(List.of(report3));

        when(adopterDogRepository.findAll()).thenReturn(List.of(expected1, expected2, expected3));

        report1.setId(1L);
        report1.setAdopterDog(expected1);
        report1.setReportDate(LocalDate.now().minusDays(1));
        report1.setFileId("Test124578");
        report1.setCaption("Рацион: гуд; Самочувствие: гуд; Поведение: гуд");
        report1.setExamination(ExaminationStatus.UNCHECKED);

        report2.setId(2L);
        report2.setAdopterDog(expected2);
        report2.setReportDate(LocalDate.now().minusDays(1));
        report2.setFileId("Test986532");
        report2.setCaption("Рацион: гуд; Самочувствие: гуд; Поведение: гуд");
        report2.setExamination(ExaminationStatus.ACCEPTED);

        report3.setId(2L);
        report3.setAdopterDog(expected3);
        report3.setReportDate(LocalDate.now().minusDays(3));
        report3.setFileId("Test986532");
        report3.setCaption("Рацион: гуд; Самочувствие: гуд; Поведение: гуд");
        report3.setExamination(ExaminationStatus.ACCEPTED);


        when(reportDogRepository.findAll()).thenReturn(List.of(report1,report2,report3));

        bot.sendAttentionForDogVolunteerAndAdopterDog();

        Thread.sleep(500);

        Mockito.verify(bot, Mockito.times(1))
                .sendMessage(0L,
                        "Внимание! Необходимо проверить отчет у "
                                + expected1.getFirstName() + " " + expected1.getLastName() + " chatID: " + expected1.getChatId());

        Mockito.verify(bot, Mockito.times(1))
                .sendMessage(0L, "Внимание! Усыновитель " + expected3.getFirstName()
                                + " " + expected3.getLastName() + " уже больше 2 дней не присылает отчеты!");
        Mockito.verify(bot, Mockito.times(3))
                .sendMessage(anyLong(), eq(MESSAGE_ATTENTION_REPORT));
    }

    @Test
    @DisplayName("Проверка отправки уведомления для волонтера и собачьего усыновителя")
    public void testSendAttentionForCatVolunteerAndAdopterCat() throws Exception {
        AdopterCat expected1 = new AdopterCat();
        AdopterCat expected2 = new AdopterCat();
        AdopterCat expected3 = new AdopterCat();

        ReportCat report1 = new ReportCat();
        ReportCat report2 = new ReportCat();
        ReportCat report3 = new ReportCat();

        expected1.setChatId(1L);
        expected1.setFirstName("Ivan");
        expected1.setLastName("Ivanov");
        expected1.setUserName("iiivanov");
        expected1.setAge(33);
        expected1.setAddress("МСК...");
        expected1.setTelephoneNumber("7951...");
        expected1.setState(PROBATION);
        expected1.setReports(List.of(report1));

        expected2.setChatId(2L);
        expected2.setFirstName("Petr");
        expected2.setLastName("Petrov");
        expected2.setUserName("pppetrov");
        expected2.setAge(33);
        expected2.setAddress("МСК...");
        expected2.setTelephoneNumber("7951...");
        expected2.setState(ADDITIONAL_PERIOD_14);
        expected1.setReports(List.of(report2));

        expected3.setChatId(3L);
        expected3.setFirstName("Denis");
        expected3.setLastName("Denisov");
        expected3.setUserName("dddenisov");
        expected3.setAge(33);
        expected3.setAddress("МСК...");
        expected3.setTelephoneNumber("7951...");
        expected3.setState(ADDITIONAL_PERIOD_30);
        expected1.setReports(List.of(report3));

        when(adopterCatRepository.findAll()).thenReturn(List.of(expected1, expected2, expected3));

        report1.setId(1L);
        report1.setAdopterCat(expected1);
        report1.setReportDate(LocalDate.now().minusDays(1));
        report1.setFileId("Test124578");
        report1.setCaption("Рацион: гуд; Самочувствие: гуд; Поведение: гуд");
        report1.setExamination(ExaminationStatus.UNCHECKED);

        report2.setId(2L);
        report2.setAdopterCat(expected2);
        report2.setReportDate(LocalDate.now().minusDays(1));
        report2.setFileId("Test986532");
        report2.setCaption("Рацион: гуд; Самочувствие: гуд; Поведение: гуд");
        report2.setExamination(ExaminationStatus.ACCEPTED);

        report3.setId(2L);
        report3.setAdopterCat(expected3);
        report3.setReportDate(LocalDate.now().minusDays(3));
        report3.setFileId("Test986532");
        report3.setCaption("Рацион: гуд; Самочувствие: гуд; Поведение: гуд");
        report3.setExamination(ExaminationStatus.ACCEPTED);


        when(reportCatRepository.findAll()).thenReturn(List.of(report1,report2,report3));

        bot.sendAttentionForCatVolunteerAndAdopterCat();

        Thread.sleep(500);

        Mockito.verify(bot, Mockito.times(1))
                .sendMessage(0L,
                        "Внимание! Необходимо проверить отчет у "
                                + expected1.getFirstName() + " " + expected1.getLastName() + " chatID: " + expected1.getChatId());

        Mockito.verify(bot, Mockito.times(1))
                .sendMessage(0L, "Внимание! Усыновитель " + expected3.getFirstName()
                        + " " + expected3.getLastName() + " уже больше 2 дней не присылает отчеты!");
        Mockito.verify(bot, Mockito.times(3))
                .sendMessage(anyLong(), eq(MESSAGE_ATTENTION_REPORT));
    }


}
