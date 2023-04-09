package com.team4.happydogbot.service;

import com.team4.happydogbot.config.BotConfig;
import com.team4.happydogbot.entity.*;
import com.team4.happydogbot.replies.Reply;
import com.team4.happydogbot.repository.AdopterCatRepository;
import com.team4.happydogbot.repository.AdopterDogRepository;
import com.team4.happydogbot.repository.ReportCatRepository;
import com.team4.happydogbot.repository.ReportDogRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.internal.verification.Times;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.team4.happydogbot.constants.BotCommands.*;
import static com.team4.happydogbot.constants.BotReplies.*;
import static com.team4.happydogbot.entity.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
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

    private final AdopterDog expectedDog = new AdopterDog();
    private final AdopterCat expectedCat = new AdopterCat();

    private Reply reply = new Reply(bot);

    @Test
    @DisplayName("Проверяем, что после нажатия на /start бот приветствует пользователя и предлагает выбрать приют")
    public void StartTest() throws TelegramApiException {
        Update update = new Update();
        update.setMessage(new Message());
        update.getMessage().setChat(new Chat());
        update.getMessage().setText(START_CMD);
        update.getMessage().getChat().setId(111111L);
        update.getMessage().getChat().setFirstName("User");

        bot.onUpdateReceived(update);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(bot, new Times(2)).execute(argumentCaptor.capture());

        List<SendMessage> actual = argumentCaptor.getAllValues();
        assertThat(actual.get(0).getChatId()).isEqualTo(update.getMessage().getChatId().toString());
        assertThat(actual.get(0).getText()).isEqualTo("User" + MESSAGE_TEXT_GREETINGS);
        assertThat(actual.get(1).getText()).isEqualTo(MESSAGE_TEXT_CHOOSE_SHELTER);
    }

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

    /**
     * Тестирование метода <b>getReport()</b> в Bot, если картинка отправлена как фото<br>
     * Mockito: когда вызывается метод <b>adopterDogRepository::findAdopterDogByChatId</b>,
     * возвращается необходимый усыновитель собак <b>adopterDog</b>
     * - проверка <b>assertThat</b> успешного сохранения отчета по всем полям
     */
    @Test
    @DisplayName("Проверка сохранения отчета, если фото отправлено как фото")
    public void getReportDogTestAsPhoto() {
        Update update = new Update();
        update.setMessage(new Message());
        update.getMessage().setCaption("Рацион: Шарик кушает прекрасно, утром чаппи из пакетика, днем сухой корм, вечером лакомство, налитую водичку за день выпивает полностью; Самочувствие: Шарик очень активно бегает, прыгает, просит с ним поиграть, к новому месту адаптировался быстро, занет где его место; Поведение: Шарик изучил дом, знает где свое место, ночью спит там, перестал лаять на членов семьи");
        ArrayList<PhotoSize> photoSizes = new ArrayList<>();
        update.getMessage().setPhoto(photoSizes);
        PhotoSize photoSize = new PhotoSize();
        photoSizes.add(photoSize);
        photoSize.setFileId("TestFileId123");
        update.getMessage().setChat(new Chat());
        update.getMessage().getChat().setId(123L);

        AdopterDog adopterDog = new AdopterDog();
        when(adopterDogRepository.findAdopterDogByChatId(any(Long.class))).thenReturn(adopterDog);
        ReportDog expected = new ReportDog();
        expected.setFileId("TestFileId123");
        expected.setCaption("Рацион: Шарик кушает прекрасно, утром чаппи из пакетика, днем сухой корм, вечером лакомство, налитую водичку за день выпивает полностью; Самочувствие: Шарик очень активно бегает, прыгает, просит с ним поиграть, к новому месту адаптировался быстро, занет где его место; Поведение: Шарик изучил дом, знает где свое место, ночью спит там, перестал лаять на членов семьи");
        adopterDog.setReports(new ArrayList<>());
        adopterDog.getReports().add(expected);

        bot.getReport(update, true);

        ArgumentCaptor<AdopterDog> argumentCaptor = ArgumentCaptor.forClass(AdopterDog.class);
        verify(adopterDogRepository).save(argumentCaptor.capture());
        AdopterDog actualAdopter = argumentCaptor.getValue();
        ReportDog actual = actualAdopter.getReports().get(0);

        Assertions.assertThat(actual.getFileId()).isEqualTo(expected.getFileId());
        Assertions.assertThat(actual.getCaption()).isEqualTo(expected.getCaption());
        Assertions.assertThat(actual.getExamination()).isEqualTo(expected.getExamination());
        Assertions.assertThat(actual.getAdopterDog()).isEqualTo(expected.getAdopterDog());
    }

    /**
     * Тестирование метода <b>getReport()</b> в Bot, если картинка отправлена как фото<br>
     * Mockito: когда вызывается метод <b>adopterDogRepository::findAdopterDogByChatId</b>,
     * возвращается необходимый усыновитель собак <b>adopterDog</b>
     * - проверка <b>assertThat</b> успешного сохранения отчета по всем полям
     */
    @Test
    @DisplayName("Проверка сохранения отчета, если фото отправлено как документ")
    public void getReportCatTestAsDocument() {
        Update update = new Update();
        update.setMessage(new Message());
        update.getMessage().setCaption("Рацион: Шарик кушает прекрасно, утром чаппи из пакетика, днем сухой корм, вечером лакомство, налитую водичку за день выпивает полностью; Самочувствие: Шарик очень активно бегает, прыгает, просит с ним поиграть, к новому месту адаптировался быстро, занет где его место; Поведение: Шарик изучил дом, знает где свое место, ночью спит там, перестал лаять на членов семьи");
        Document document = new Document();
        update.getMessage().setDocument(document);
        document.setFileId("TestFileId123");
        update.getMessage().setChat(new Chat());
        update.getMessage().getChat().setId(123L);

        AdopterCat adopterCat = new AdopterCat();
        when(adopterCatRepository.findAdopterCatByChatId(any(Long.class))).thenReturn(adopterCat);
        ReportCat expected = new ReportCat();
        expected.setFileId("TestFileId123");
        expected.setCaption("Рацион: Шарик кушает прекрасно, утром чаппи из пакетика, днем сухой корм, вечером лакомство, налитую водичку за день выпивает полностью; Самочувствие: Шарик очень активно бегает, прыгает, просит с ним поиграть, к новому месту адаптировался быстро, занет где его место; Поведение: Шарик изучил дом, знает где свое место, ночью спит там, перестал лаять на членов семьи");
        adopterCat.setReports(new ArrayList<>());
        adopterCat.getReports().add(expected);

        bot.getReport(update, false);

        ArgumentCaptor<AdopterCat> argumentCaptor = ArgumentCaptor.forClass(AdopterCat.class);
        verify(adopterCatRepository).save(argumentCaptor.capture());
        AdopterCat actualAdopter = argumentCaptor.getValue();
        ReportCat actual = actualAdopter.getReports().get(0);

        Assertions.assertThat(actual.getFileId()).isEqualTo(expected.getFileId());
        Assertions.assertThat(actual.getCaption()).isEqualTo(expected.getCaption());
        Assertions.assertThat(actual.getExamination()).isEqualTo(expected.getExamination());
        Assertions.assertThat(actual.getAdopterCat()).isEqualTo(expected.getAdopterCat());
    }

    /**
     * Тестирование метода <b>getReport()</b> в Bot, если подпись к фото не прошла валидацию<br>
     */
    @Test
    @DisplayName("Проверка сохранения отчета, если подпись к фото не прошла валидацию - отчет на сохранен")
    public void getReportTestWithIncorrectCaption() {
        Update update = new Update();
        update.setMessage(new Message());
        update.getMessage().setCaption("Рацион: Гуд; Самочувствие: гуд; Поведение: гуд");
        update.getMessage().setChat(new Chat());
        update.getMessage().getChat().setId(123L);

        bot.getReport(update, true);
    }

    /**
     * Тестирование метода <b>changeUserStatusOfShelter()</b> в Bot, если новый пользователь выбрал приют для собак<br>
     * Mockito: когда вызывается метод <b>adopterDogRepository::findAdopterDogByChatId</b> возвращается <b>null</b>
     * , когда вызывается метод <b>adopterCatRepository::findAdopterCatByChatId</b> возвращается <b>null</b>
     * - проверка <b>assertThat</b> успешного изменения статуса у усыновителя
     */
    @Test
    @DisplayName("Проверка изменения статуса пользователя на приют для собак, новый пользователь")
    public void changeUserStatusOfShelterToDogNewUserTest() {
        Long chatId = 123L;
        boolean isDog = true;

        when(adopterDogRepository.findAdopterDogByChatId(any(Long.class))).thenReturn(null);
        when(adopterCatRepository.findAdopterCatByChatId(any(Long.class))).thenReturn(null);
        expectedDog.setChatId(chatId);
        expectedDog.setDog(isDog);

        bot.changeUserStatusOfShelter(chatId, isDog);

        ArgumentCaptor<AdopterDog> argumentCaptor = ArgumentCaptor.forClass(AdopterDog.class);
        verify(adopterDogRepository).save(argumentCaptor.capture());
        AdopterDog actualAdopter = argumentCaptor.getValue();

        Assertions.assertThat(actualAdopter.getChatId()).isEqualTo(expectedDog.getChatId());
        Assertions.assertThat(actualAdopter.isDog()).isEqualTo(expectedDog.isDog());
    }

    /**
     * Тестирование метода <b>changeUserStatusOfShelter()</b> в Bot, если пользователь был в приюте для кошек и выбрал приют для собак<br>
     * Mockito: когда вызывается метод <b>adopterDogRepository::findAdopterDogByChatId</b> возвращается <b>null</b>
     * , когда вызывается метод <b>adopterCatRepository::findAdopterCatByChatId</b> возвращается усыновитель кошек <b>adopterCat</b>
     * - проверка <b>assertThat</b> успешного изменения статуса у усыновителя
     */
    @Test
    @DisplayName("Проверка изменения статуса пользователя на приют для собак, пользователь уже был в приюте кошек")
    public void changeUserStatusOfShelterToDogWhenWasCatTest() {
        Long chatId = 123L;
        boolean isDog = true;

        AdopterCat adopterCat = new AdopterCat();
        adopterCat.setChatId(chatId);
        adopterCat.setDog(false);
        when(adopterDogRepository.findAdopterDogByChatId(any(Long.class))).thenReturn(null);
        when(adopterCatRepository.findAdopterCatByChatId(any(Long.class))).thenReturn(adopterCat);
        expectedDog.setChatId(chatId);
        expectedCat.setChatId(chatId);
        expectedDog.setDog(isDog);
        expectedCat.setDog(isDog);

        bot.changeUserStatusOfShelter(chatId, isDog);

        ArgumentCaptor<AdopterDog> argumentCaptorDog = ArgumentCaptor.forClass(AdopterDog.class);
        verify(adopterDogRepository).save(argumentCaptorDog.capture());
        AdopterDog actualAdopterDog = argumentCaptorDog.getValue();
        ArgumentCaptor<AdopterCat> argumentCaptorCat = ArgumentCaptor.forClass(AdopterCat.class);
        verify(adopterCatRepository).save(argumentCaptorCat.capture());
        AdopterCat actualAdopterCat = argumentCaptorCat.getValue();

        Assertions.assertThat(actualAdopterDog.getChatId()).isEqualTo(expectedDog.getChatId());
        Assertions.assertThat(actualAdopterDog.isDog()).isEqualTo(expectedDog.isDog());
        Assertions.assertThat(actualAdopterCat.getChatId()).isEqualTo(expectedCat.getChatId());
        Assertions.assertThat(actualAdopterCat.isDog()).isEqualTo(expectedCat.isDog());
    }

    /**
     * Тестирование метода <b>changeUserStatusOfShelter()</b> в Bot, если новый пользователь выбрал приют для кошек<br>
     * Mockito: когда вызывается метод <b>adopterDogRepository::findAdopterDogByChatId</b> возвращается <b>null</b>
     * , когда вызывается метод <b>adopterCatRepository::findAdopterCatByChatId</b> возвращается <b>null</b>
     * - проверка <b>assertThat</b> успешного изменения статуса у усыновителя
     */
    @Test
    @DisplayName("Проверка изменения статуса пользователя на приют для кошек, новый пользователь")
    public void changeUserStatusOfShelterToCatNewUserTest() {
        Long chatId = 123L;
        boolean isDog = false;

        when(adopterDogRepository.findAdopterDogByChatId(any(Long.class))).thenReturn(null);
        when(adopterCatRepository.findAdopterCatByChatId(any(Long.class))).thenReturn(null);
        expectedCat.setChatId(chatId);
        expectedCat.setDog(isDog);

        bot.changeUserStatusOfShelter(chatId, isDog);

        ArgumentCaptor<AdopterCat> argumentCaptor = ArgumentCaptor.forClass(AdopterCat.class);
        verify(adopterCatRepository).save(argumentCaptor.capture());
        AdopterCat actualAdopter = argumentCaptor.getValue();

        Assertions.assertThat(actualAdopter.getChatId()).isEqualTo(expectedCat.getChatId());
        Assertions.assertThat(actualAdopter.isDog()).isEqualTo(expectedCat.isDog());
    }

    /**
     * Тестирование метода <b>changeUserStatusOfShelter()</b> в Bot, если пользователь был в приюте для собак и выбрал приют для кошек<br>
     * Mockito: когда вызывается метод <b>adopterDogRepository::findAdopterDogByChatId</b> возвращается усыновитель собак <b>adopterDog</b>
     * , когда вызывается метод <b>adopterCatRepository::findAdopterCatByChatId</b> возвращается <b>null</b>
     * - проверка <b>assertThat</b> успешного изменения статуса у усыновителя
     */
    @Test
    @DisplayName("Проверка изменения статуса пользователя на приют для кошек, пользователь уже был в приюте собак")
    public void changeUserStatusOfShelterToCatWhenWasDogTest() {
        Long chatId = 123L;
        boolean isDog = false;

        AdopterDog adopterDog = new AdopterDog();
        adopterDog.setChatId(chatId);
        adopterDog.setDog(true);
        when(adopterDogRepository.findAdopterDogByChatId(any(Long.class))).thenReturn(adopterDog);
        when(adopterCatRepository.findAdopterCatByChatId(any(Long.class))).thenReturn(null);
        expectedDog.setChatId(chatId);
        expectedCat.setChatId(chatId);
        expectedDog.setDog(isDog);
        expectedCat.setDog(isDog);

        bot.changeUserStatusOfShelter(chatId, isDog);

        ArgumentCaptor<AdopterDog> argumentCaptorDog = ArgumentCaptor.forClass(AdopterDog.class);
        verify(adopterDogRepository).save(argumentCaptorDog.capture());
        AdopterDog actualAdopterDog = argumentCaptorDog.getValue();
        ArgumentCaptor<AdopterCat> argumentCaptorCat = ArgumentCaptor.forClass(AdopterCat.class);
        verify(adopterCatRepository).save(argumentCaptorCat.capture());
        AdopterCat actualAdopterCat = argumentCaptorCat.getValue();

        Assertions.assertThat(actualAdopterDog.getChatId()).isEqualTo(expectedDog.getChatId());
        Assertions.assertThat(actualAdopterDog.isDog()).isEqualTo(expectedDog.isDog());
        Assertions.assertThat(actualAdopterCat.getChatId()).isEqualTo(expectedCat.getChatId());
        Assertions.assertThat(actualAdopterCat.isDog()).isEqualTo(expectedCat.isDog());
    }

    /**
     * Тестирование метода <b>forwardMessageToVolunteer()</b> в Bot<br>
     * - проверка <b>assertThat</b> успешного перенаправления сообщения пользователю
     */
    @Test
    @DisplayName("Проверка метода пересылки сообщения волонтеру от пользователя")
    public void forwardMessageToVolunteerTest() throws TelegramApiException {
        Long chatId = 123L;
        int messageId = 456;

        bot.forwardMessageToVolunteer(chatId,messageId);

        ArgumentCaptor<ForwardMessage> argumentCaptor = ArgumentCaptor.forClass(ForwardMessage.class);
        verify(bot).execute(argumentCaptor.capture());
        ForwardMessage actual = argumentCaptor.getValue();
        assertThat(actual.getFromChatId()).isEqualTo(chatId.toString());
        assertThat(actual.getMessageId()).isEqualTo(messageId);
    }

    /**
     * Тестирование метода <b>talkWithVolunteerOrNoSuchCommand()</b> в Bot<br>
     * - проверка <b>assertThat</b> успешного направления сообщения пользователю что нет такой команды
     */
    @Test
    @DisplayName("Проверка метода разговора с волонтером, нет такой команды")
    public void talkWithVolunteerOrNoSuchCommandTest() throws TelegramApiException {
        Update update = new Update();
        update.setMessage(new Message());
        update.getMessage().setChat(new Chat());
        update.getMessage().getChat().setId(123L);
        update.getMessage().setText("Test message");

        bot.talkWithVolunteerOrNoSuchCommand(update);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(bot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getChatId()).isEqualTo("123");
        assertThat(actual.getText()).isEqualTo("Нет такой команды");
    }
}
