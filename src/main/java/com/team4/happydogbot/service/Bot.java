package com.team4.happydogbot.service;

import com.team4.happydogbot.config.BotConfig;
import com.team4.happydogbot.entity.Adopter;
import com.team4.happydogbot.entity.Report;
import com.team4.happydogbot.replies.Reply;
import com.team4.happydogbot.repository.AdopterRepository;
import com.team4.happydogbot.repository.ReportRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.team4.happydogbot.constants.BotCommands.*;
import static com.team4.happydogbot.constants.BotReplies.*;
import static com.team4.happydogbot.entity.Status.ADDITIONAL_PERIOD;
import static com.team4.happydogbot.entity.Status.PROBATION;

@Slf4j
@Service
public class Bot extends TelegramLongPollingBot {
    final BotConfig config;

    private final AdopterRepository adopterRepository;

    private final ReportRepository reportRepository;



    public Bot(BotConfig config, AdopterRepository adopterRepository, ReportRepository reportRepository) {
        this.config = config;
        this.adopterRepository = adopterRepository;
        this.reportRepository = reportRepository;
    }

    public static final long VOLUNTEER_ID = 1607411391;
    public static final HashMap<String, Long> REQUEST_FROM_USER = new HashMap<>();
    public boolean isDog = true;

    Reply reply = new Reply(this);


    @Override
    public String getBotUsername() {
        return config.getName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }


    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            if (!isDog && reply.catReplies.containsKey(messageText)) {
                reply.catReplies.get(messageText).accept(chatId);
            } else if (reply.dogReplies.containsKey(messageText)) {
                reply.dogReplies.get(messageText).accept(chatId);

            } else if (START_CMD.equals(messageText)) {
                sendMessage(chatId, update.getMessage().getChat().getFirstName() + MESSAGE_TEXT_GREETINGS);
                sendMessage(chatId, MESSAGE_TEXT_CHOOSE_SHELTER, replyKeyboardShelter());
            } else if (CALL_VOLUNTEER_CMD.equals(messageText)) {
                REQUEST_FROM_USER.put(messageText, chatId);
                sendMessageWithInlineKeyboard(chatId, MESSAGE_TEXT_WRITE_VOLUNTEER, FINISH_VOLUNTEER_CMD);
                // Создаем мапу и кладем в нее сообщение в кач-ве ключа и chatId в кач-ве значения того, кто позвал волонтера,
                // то есть пока в мапе лежит текст и chatId - это значит что юзер находится в состоянии разговора с волонтером,
                // отправляем сообщение пользователю
            } else talkWithVolunteerOrNoSuchCommand(chatId, update);

        } else if (update.hasCallbackQuery()) {
            String messageData = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            if (!isDog && reply.catReplies.containsKey(messageData)) {
                reply.catReplies.get(messageData).accept(chatId);
            } else if (reply.dogReplies.containsKey(messageData)) {
                reply.dogReplies.get(messageData).accept(chatId);

            } else if (CALL_VOLUNTEER_CMD.equals(messageData)) {
                REQUEST_FROM_USER.put(messageData, chatId);
                sendMessageWithInlineKeyboard(chatId, MESSAGE_TEXT_WRITE_VOLUNTEER, FINISH_VOLUNTEER_CMD);
            } else if (SEND_REPORT_CMD.equals(messageData) && isDog) {
                //МЕТОД ОТПРАВКИ КОНТАКТНЫХ ДАННЫХ в таблицу для собак
            } else if (SEND_REPORT_CMD.equals(messageData)) {
                //МЕТОД ОТПРАВКИ КОНТАКТНЫХ ДАННЫХ в таблицу для кошек
            } else if (SEND_CONTACT_CMD.equals(messageData) && isDog) {
                //метод для отправки отчета в таблицу для собак
            } else if (SEND_CONTACT_CMD.equals(messageData)) {
                //метод для отправки отчет в таблица для кошек
            } else sendMessage(chatId, MESSAGE_TEXT_NO_COMMAND);
        }

    }

    //@Tamara пока проверяю работу метода
    public void sendDocument(long chatId) {
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(String.valueOf(chatId));
        sendDocument.setCaption("Информация по вашему вопросу сожержится в файле");
        sendDocument.setDocument(new InputFile(" "));
        try {
            execute(sendDocument);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }

    /**
     * Отправляет сообщение
     *
     * @param chatId     идентификатор пользователя
     * @param textToSend текст сообщения
     * @throws TelegramApiException
     */
    public void sendMessage(long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }

    /**
     * Отправляет сообщение c клавиатурой
     *
     * @param chatId     идентификатор пользователя
     * @param textToSend текст сообщения
     * @param keyboard   клавиатура
     * @throws TelegramApiException
     */
    public void sendMessage(long chatId, String textToSend, ReplyKeyboard keyboard) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        sendMessage.setReplyMarkup(keyboard);
        sendMessage.setParseMode(ParseMode.HTML);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }

    /**
     * Отправляет сообщение с InlineKeyboard<br>
     * Используется методы
     * {@link #sendMessage(long, String, ReplyKeyboard)}
     * {@link #InlineKeyboardMaker(String...)}
     *
     * @param chatId     идентификатор пользователя
     * @param textToSend текст сообщения
     * @param buttons    множество (массив или varargs) кнопок клавиатуры
     */
    public void sendMessageWithInlineKeyboard(long chatId, String textToSend, String... buttons) {
        InlineKeyboardMarkup inlineKeyboard = InlineKeyboardMaker(buttons);
        sendMessage(chatId, textToSend, inlineKeyboard);
    }

    /**
     * Создает InlineKeyboard
     *
     * @param buttons множество (массив или varargs) кнопок клавиатуры
     * @return клавиатура привязанная к сообщению
     */
    public InlineKeyboardMarkup InlineKeyboardMaker(String... buttons) {
        InlineKeyboardMarkup inlineKeyboardAbout = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        //создаем кнопки
        for (String buttonText :
                buttons) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(buttonText);
            button.setCallbackData(buttonText);
            List<InlineKeyboardButton> rowInLine1 = new ArrayList<>();
            rowInLine1.add(button);
            rowsInLine.add(rowInLine1);
        }
        inlineKeyboardAbout.setKeyboard(rowsInLine);

        return inlineKeyboardAbout;
    }


    /**
     * Создает клавиатуру внизу экрана
     * Эта клавиатура всегда доступна пользователю
     *
     * @return клавиатура с вариантами команд
     */
    public ReplyKeyboardMarkup replyKeyboardBottom() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>();

        // Первая строчка клавиатуры
        KeyboardRow keyboardRow1 = new KeyboardRow();
        // Добавляем кнопки в первую строчку клавиатуры
        keyboardRow1.add(SHELTER_INFO_CMD);
        keyboardRow1.add(PET_INFO_CMD);

        // Вторая строчка клавиатуры
        KeyboardRow keyboardRow2 = new KeyboardRow();
        // Добавляем кнопки во вторую строчку клавиатуры
        keyboardRow2.add(SEND_REPORT_CMD);
        keyboardRow2.add(CALL_VOLUNTEER_CMD);

        KeyboardRow keyboardRow3 = new KeyboardRow();
        keyboardRow3.add(SHELTER_CHOOSE);

        // Добавляем все строчки клавиатуры в список
        keyboard.add(keyboardRow1);
        keyboard.add(keyboardRow2);
        keyboard.add(keyboardRow3);

        // и устанавливаем этот список нашей клавиатуре
        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }

    /**
     * Создает клавиатуру для выбора приюта внизу экрана
     *
     * @return клавиатура с вариантами команд
     */
    public ReplyKeyboardMarkup replyKeyboardShelter() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(SHELTER_CAT);
        keyboardRow1.add(SHELTER_DOG);
        keyboard.add(keyboardRow1);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    /**
     * Находит и удаляет последний запрос волонтеру от пользователя по chatId пользователя
     *
     * @param chatId идентификатор чата пользователя, который позвал волонтера и написал сообщение волонтеру
     */
    public void findAndRemoveRequestFromUser(long chatId) {
        for (Map.Entry<String, Long> stringLongEntry : REQUEST_FROM_USER.entrySet()) {
            if (stringLongEntry.getValue() == chatId) {
                REQUEST_FROM_USER.remove(stringLongEntry.getKey());
                break;
            }
        }
    }

    /**
     * Пересылает волонтеру сообщение с messageId от пользователя с chatId пользователя, позвавшего волонтера
     *
     * @param chatId    идентификатор чата пользователя, который позвал волонтера и написал сообщение волонтеру
     * @param messageId идентификатор пересылаемого волонтеру сообщения
     */
    private void forwardMessageToVolunteer(long chatId, int messageId) {
        ForwardMessage forwardMessage = new ForwardMessage(String.valueOf(VOLUNTEER_ID), String.valueOf(chatId), messageId);
        try {
            execute(forwardMessage);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }

//    /**
//     * Изменяет текст существующего сообщения, обычно после нажатия пользователем кнопки InlineKeyboardMaker
//     * @param chatId идентификатор чата пользователя, в котором произошло действие и происходит изменение текста сообщения
//     * @param messageId идентификатор сообщения, которое изменяется
//     */
//    private void executeEditMessageText(long chatId, long messageId) {
//        EditMessageText message = new EditMessageText();
//        message.setChatId(String.valueOf(chatId));
//        message.setText(TALK_ENDED);
//        message.setMessageId((int) messageId);
//        try {
//            execute(message);
//        } catch (TelegramApiException e) {
//            log.error("Error occurred: " + e.getMessage());
//        }
//    }

    /**
     * Метод описывает состояние разговора с волонтером или отправка дефолтной команды, а именно:<br>
     * - либо пересылает сообщение волонтера от пользователя,<br>
     * - либо отправляет сообщение пользователю от волонтера,<br>
     * - либо если пользователь не находится в состоянии разговора с волонтером, сообщает что такой команды нет<br>
     * Используются методы:<br>
     * {@link #findAndRemoveRequestFromUser(long chatId)}<br>
     * {@link #forwardMessageToVolunteer(long chatId, int messageId)}<br>
     * {@link #sendMessage(long chatId, String textToSend)}
     *
     * @param chatId идентификатор чата пользователя, который позвал волонтера и написал сообщение волонтеру,
     *               либо волонтера, которы ответил пользователю
     * @param update принятое текстовое сообщение пользователя<br>
     */
    private void talkWithVolunteerOrNoSuchCommand(long chatId, Update update) {
        if (REQUEST_FROM_USER.containsValue(chatId)) {
            // Если в мапе уже есть chatId того кто написал боту, то есть продолжается общение с волонтером,
            // то удаляем предыдущее сообщение и записываем новое сообщение, отправляем сообщение волонтеру
            findAndRemoveRequestFromUser(chatId);
            REQUEST_FROM_USER.put(update.getMessage().getText(), chatId);
            forwardMessageToVolunteer(chatId, update.getMessage().getMessageId());
            sendMessage(chatId, MESSAGE_TEXT_WAS_SENT);
        } else if (VOLUNTEER_ID == chatId
                // Если сообщение поступило от волонтера и содержит Reply на другое сообщение и текст в
                // Reply совпадает с тем что в мапе,то это сообщение отправляем юзеру
                && update.getMessage().getReplyToMessage() != null
                && REQUEST_FROM_USER.containsKey(update.getMessage().getReplyToMessage().getText())) {
            String s = update.getMessage().getReplyToMessage().getText();
            sendMessageWithInlineKeyboard(
                    REQUEST_FROM_USER.get(s), // получаем chatId по сообщению на которое отвечаем
                    "Сообщение от волонтера " + update.getMessage().getChat().getFirstName() + ":\n<i>" +
                            update.getMessage().getText() + "</i>\n" + "\n" + MESSAGE_TEXT_WRITE_VOLUNTEER,
                    FINISH_VOLUNTEER_CMD);
        } else {
            // Если сообщение не подходит не под одну команду и волонтер и юзер не находятся в состоянии
            // разговора, то выводим сообщение нет такой команды
            sendMessage(chatId, MESSAGE_TEXT_NO_COMMAND);
        }
    }

    /**
     * Метод организует по расписанию автоматическую проверку наличия отчетов со сроком регистрации равным 2 и более
     * дней от текущей даты по следующему алгоритму:<br>
     * - получение списка Adopter со статусом PROBATION;<br>
     * - получение для каждого adopter из списка отчетов последний отчет;<br>
     * - отправка сообщения волонтеру по итогу проверки на разницу дня года текущей даты и дня года даты регистрации отчета
     */
    @Scheduled(cron = "30 30 8 * * *")
    private void sendNoteForVolunteer() {

        List<Adopter> adopters = adopterRepository.findAll();
        List<Adopter> adoptersWithProbationPeriod = adopters.stream().filter(x -> (x.getState() == PROBATION)||
                (x.getState() == ADDITIONAL_PERIOD)).toList();
        List<Report> reports = reportRepository.findAll();
        for (Adopter adopter : adoptersWithProbationPeriod) {
            Report report = reports.stream().filter(x -> (x.getAdopter().equals(adopter))
                            && (x.getExamination()))
                    .reduce((first, last) -> last)
                    .orElseThrow();
            //для проверки рабоспособности в условии ниже добавить +3 после LocalDate.now().getDayOfYear()
            if(LocalDate.now().getDayOfYear()  - report.getReportDate().getDayOfYear() >=2){
                sendMessage(VOLUNTEER_ID, "Внимание! Усыновитель "+ adopter.getFirstName()
                        + " " + adopter.getLastName() + " уже больше 2 дней не присылает отчеты!");
            }
        }
    }
}
