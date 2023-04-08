package com.team4.happydogbot.service;

import com.team4.happydogbot.config.BotConfig;
import com.team4.happydogbot.constants.BotCommands;
import com.team4.happydogbot.entity.*;
import com.team4.happydogbot.replies.Reply;
import com.team4.happydogbot.repository.AdopterCatRepository;
import com.team4.happydogbot.repository.AdopterDogRepository;
import com.team4.happydogbot.repository.ReportCatRepository;
import com.team4.happydogbot.repository.ReportDogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.util.*;

import static com.team4.happydogbot.constants.BotCommands.*;
import static com.team4.happydogbot.constants.BotReplies.*;
import static com.team4.happydogbot.constants.PatternValidation.validationPatternReport;
import static com.team4.happydogbot.entity.Status.*;

@Slf4j
@Service
public class Bot extends TelegramLongPollingBot {
    private BotConfig config;

    private AdopterDogRepository adopterDogRepository;

    private AdopterCatRepository adopterCatRepository;

    private ReportDogRepository reportDogRepository;
    private ReportCatRepository reportCatRepository;
    private AdopterDogService adopterDogService;
    private AdopterCatService adopterCatService;

    @Autowired
    public Bot(BotConfig config, AdopterDogRepository adopterDogRepository, AdopterCatRepository adopterCatRepository,
               ReportDogRepository reportDogRepository, ReportCatRepository reportCatRepository,
               AdopterDogService adopterDogService, AdopterCatService adopterCatService) {

        this.config = config;
        this.adopterDogRepository = adopterDogRepository;
        this.adopterCatRepository = adopterCatRepository;
        this.reportDogRepository = reportDogRepository;
        this.reportCatRepository = reportCatRepository;
        this.adopterDogService = adopterDogService;
        this.adopterCatService = adopterCatService;
    }

    public static final HashMap<String, Long> REQUEST_FROM_USER = new HashMap<>();

    public static final HashSet<Long> REQUEST_GET_REPLY_FROM_USER = new HashSet<>();

    Reply reply = new Reply(this);

    public Bot() {

    }

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
            if (START_CMD.equals(messageText)) {
                sendMessage(chatId, update.getMessage().getChat().getFirstName() + MESSAGE_TEXT_GREETINGS);
                sendMessage(chatId, MESSAGE_TEXT_CHOOSE_SHELTER, replyKeyboardShelter());
            } else if ((adopterCatRepository.findAdopterCatByChatId(chatId) != null &&
                    !adopterCatRepository.findAdopterCatByChatId(chatId).isDog()) &&
                    reply.catReplies.containsKey(messageText)) {
                reply.catReplies.get(messageText).accept(chatId);
            } else if (reply.dogReplies.containsKey(messageText)) {
                reply.dogReplies.get(messageText).accept(chatId);
            } else if (CALL_VOLUNTEER_CMD.equals(messageText)) {
                REQUEST_FROM_USER.put(messageText, chatId);
                sendMessageWithInlineKeyboard(chatId, MESSAGE_TEXT_WRITE_VOLUNTEER, FINISH_VOLUNTEER_CMD);
                // Кладем в HashMap сообщение в кач-ве ключа и chatId в кач-ве значения того, кто позвал волонтера,
                // то есть пока в HashMap лежит текст и chatId - это значит что юзер находится в состоянии разговора с волонтером,
                // отправляем сообщение пользователю
            } else if (REQUEST_GET_REPLY_FROM_USER.contains(chatId)) {
                sendMessageWithInlineKeyboard(update.getMessage().getChatId(), MESSAGE_TEXT_NO_REPORT_PHOTO, REPORT_EXAMPLE, SEND_REPORT);
            } else talkWithVolunteerOrNoSuchCommand(chatId, update);

        } else if (update.hasCallbackQuery()) {
            String messageData = update.getCallbackQuery().getData();
            String messageText = update.getCallbackQuery().getMessage().getText();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            if (adopterCatRepository.findAdopterCatByChatId(chatId) != null &&
                    !adopterCatRepository.findAdopterCatByChatId(chatId).isDog() &&
                    reply.catReplies.containsKey(messageData)) {
                reply.catReplies.get(messageData).accept(chatId);
            } else if (reply.dogReplies.containsKey(messageData)) {
                reply.dogReplies.get(messageData).accept(chatId);
            } else if (CALL_VOLUNTEER_CMD.equals(messageData)) {
                REQUEST_FROM_USER.put(messageData, chatId);
                sendMessageWithInlineKeyboard(chatId, MESSAGE_TEXT_WRITE_VOLUNTEER, FINISH_VOLUNTEER_CMD);
            } else if (SEND_REPORT.equals(messageData)) {
                REQUEST_GET_REPLY_FROM_USER.add(chatId);
                sendMessage(chatId, MESSAGE_TEXT_PRE_REPORT);
                // Кладем в HashSet chatId пользователя, который нажал кнопку "Отправить отчет", то есть пока в HashMap
                // лежит chatId - это значит что юзер находится в состоянии отправки отчета,
                // отправляем сообщение пользователю
            } else if (FINISH_PROBATION.equals(messageData)
                    && adopterDogRepository.findAdopterDogByChatId(chatId).isDog()) {
                //метод изменения статуса на Finished и информирования пользователя для собак
                changeDogAdopterStatus(MESSAGE_DECISION_FINISH, messageText, FINISHED_PROBATION_PERIOD);
            } else if (FINISH_PROBATION.equals(messageData)) {
                //метод изменения статуса на Finished и информирования пользователя для кошек
                changeCatAdopterStatus(MESSAGE_DECISION_FINISH, messageText, FINISHED_PROBATION_PERIOD);
            } else if (EXTEND_PROBATION_14.equals(messageData)
                    && adopterDogRepository.findAdopterDogByChatId(chatId).isDog()) {
                //метод изменения статуса на Additional_14 и информирования пользователя для собак
                changeDogAdopterStatus(MESSAGE_DECISION_EXTEND_14, messageText, ADDITIONAL_PERIOD_14);
            } else if (EXTEND_PROBATION_14.equals(messageData)) {
                //метод изменения статуса на Additional_14 и информирования пользователя для кошек
                changeCatAdopterStatus(MESSAGE_DECISION_EXTEND_14, messageText, ADDITIONAL_PERIOD_14);
            } else if (EXTEND_PROBATION_30.equals(messageData)
                    && adopterDogRepository.findAdopterDogByChatId(chatId).isDog()) {
                //метод изменения статуса на Additional_30 и информирования пользователя для собак
                changeDogAdopterStatus(MESSAGE_DECISION_EXTEND_30, messageText, ADDITIONAL_PERIOD_30);
            } else if (EXTEND_PROBATION_30.equals(messageData)) {
                //метод изменения статуса на Additional_30 и информирования пользователя для кошек
                changeCatAdopterStatus(MESSAGE_DECISION_EXTEND_30, messageText, ADDITIONAL_PERIOD_30);
            } else if (REFUSE.equals(messageData)
                    && adopterDogRepository.findAdopterDogByChatId(chatId).isDog()) {
                //метод изменения статуса на Refuse и информирования пользователя для собак
                changeDogAdopterStatus(MESSAGE_DECISION_REFUSE, messageText, ADOPTION_DENIED);
            } else if (REFUSE.equals(messageData)) {
                //метод изменения статуса на Refuse и информирования пользователя для кошек
                changeCatAdopterStatus(MESSAGE_DECISION_REFUSE, messageText, ADOPTION_DENIED);
            } else sendMessage(chatId, MESSAGE_TEXT_NO_COMMAND);

        } else if (update.hasMessage() && (update.getMessage().hasPhoto() || update.getMessage().hasDocument())) {
            long chatId = update.getMessage().getChatId();
            if (REQUEST_GET_REPLY_FROM_USER.contains(chatId) &&
                    adopterDogRepository.findAdopterDogByChatId(chatId) != null &&
                    adopterDogRepository.findAdopterDogByChatId(chatId).isDog()) {
                if (update.getMessage().getCaption() == null) {
                    sendMessageWithInlineKeyboard(update.getMessage().getChatId(), MESSAGE_TEXT_NO_REPORT_TEXT, REPORT_EXAMPLE, SEND_REPORT);
                } else {
                    getReport(update, true);
                }
            } else if (REQUEST_GET_REPLY_FROM_USER.contains(chatId)) {
                if (update.getMessage().getCaption() == null) {
                    sendMessageWithInlineKeyboard(update.getMessage().getChatId(), MESSAGE_TEXT_NO_REPORT_TEXT, REPORT_EXAMPLE, SEND_REPORT);
                } else {
                    getReport(update, false);
                }

            } else sendMessage(chatId, MESSAGE_TEXT_NO_COMMAND);

        } else if (update.hasMessage() && update.getMessage().hasContact()) {
            long chatId = update.getMessage().getChatId();
            processContact(update);
            sendMessage(chatId, MESSAGE_TEXT_SEND_CONTACT_SUCCESS);
        }
    }

    /**
     * Метод проверяет текстовую часть отчета на соответствие шаблону, если это фото, выбирает максимальный размер,
     * если это файл, получает fileId и записывает данные отчета в базу кошек или собак, убирает состояние отправки
     * отчета - удаляет chatId из HashSet где хранятся пользоватили нажавшие кнопку "Отправить отчет"
     * Используются методы:<br>
     * {@link #sendMessageWithInlineKeyboard(long chatId, String textToSend, String... buttons)}<br>
     * {@link #sendMessage(long chatId, String textToSend)}
     *
     * @param update входящий апдейт бота с фото или файлом (файлом на случай отправки пользователем фото без сжатия)
     * @param isDog  состояние выбранного приюта у Adopter
     */
    public void getReport(Update update, boolean isDog) {
        if (validationPatternReport(update.getMessage().getCaption())) {
            String reportText = update.getMessage().getCaption();
            String fileId;
            if (update.getMessage().hasPhoto()) {
                List<PhotoSize> photoSizes = update.getMessage().getPhoto();
                PhotoSize photoSize = photoSizes.stream()
                        .max(Comparator.comparing(PhotoSize::getFileSize)).orElse(null);
                fileId = photoSize.getFileId();
            } else {
                Document document = update.getMessage().getDocument();
                fileId = document.getFileId();
            }
            if (isDog) {
                AdopterDog adopterDog = adopterDogRepository.findAdopterDogByChatId(update.getMessage().getChatId());
                ReportDog reportDog = new ReportDog();
                reportDog.setReportDate(LocalDate.now());
                reportDog.setFileId(fileId);
                reportDog.setCaption(reportText);
                reportDog.setAdopterDog(adopterDog);
                adopterDog.getReports().add(reportDog);
                adopterDogRepository.save(adopterDog);
            } else {
                AdopterCat adopterCat = adopterCatRepository.findAdopterCatByChatId(update.getMessage().getChatId());
                ReportCat reportCat = new ReportCat();
                reportCat.setReportDate(LocalDate.now());
                reportCat.setFileId(fileId);
                reportCat.setCaption(reportText);
                reportCat.setAdopterCat(adopterCat);
                adopterCat.getReports().add(reportCat);
                adopterCatRepository.save(adopterCat);
            }
            REQUEST_GET_REPLY_FROM_USER.remove(update.getMessage().getChatId());
            sendMessage(update.getMessage().getChatId(), MESSAGE_THANKS_FOR_REPLY);
        } else {
            REQUEST_GET_REPLY_FROM_USER.remove(update.getMessage().getChatId());
            sendMessageWithInlineKeyboard(update.getMessage().getChatId(), MESSAGE_TEXT_NOT_LIKE_EXAMPLE, REPORT_EXAMPLE, SEND_REPORT);
        }
    }

    /**
     * Метод отправляет пользователю фото с подписью
     *
     * @param chatId  идентификатор пользователя
     * @param fileUrl URL фото, фото должно храниться на сервере
     */
    public void sendPhotoWithCaption(long chatId, String caption, String fileUrl) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(String.valueOf(chatId));
        sendPhoto.setCaption(caption);
        sendPhoto.setPhoto(new InputFile(fileUrl));
        sendPhoto.setParseMode(ParseMode.HTML);
        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }

    /**
     * Метод отправляет пользователю фото с подписью
     *
     * @param chatId   идентификатор пользователя
     * @param fileUrl  URL фото, фото должно храниться на сервере
     * @param keyboard клавиатура
     */
    public void sendPhotoWithCaption(long chatId, String caption, String fileUrl, ReplyKeyboard keyboard) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(String.valueOf(chatId));
        sendPhoto.setCaption(caption);
        sendPhoto.setPhoto(new InputFile(fileUrl));
        sendPhoto.setReplyMarkup(keyboard);
        sendPhoto.setParseMode(ParseMode.HTML);
        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }

    /**
     * Метод отправляет пользователю фото с подписью с InlineKeyboard<br>
     * Используется методы
     * {@link #sendPhotoWithCaption(long, String, String, ReplyKeyboard)}
     * {@link #InlineKeyboardMaker(String...)}
     *
     * @param chatId     идентификатор пользователя
     * @param textToSend текст сообщения
     * @param fileUrl    URL фото, фото должно храниться на сервере
     * @param buttons    множество (массив или varargs) кнопок клавиатуры
     */
    public void sendPhotoWithCaptionWithInlineKeyboard(long chatId, String textToSend, String fileUrl, String... buttons) {
        InlineKeyboardMarkup inlineKeyboard = InlineKeyboardMaker(buttons);
        sendPhotoWithCaption(chatId, textToSend, fileUrl, inlineKeyboard);
    }

    /**
     * Метод отправляет пользователю документ
     *
     * @param chatId  идентификатор пользователя
     * @param fileUrl URL документа, документ должен храниться на сервере
     */
    public void sendDocument(long chatId, String fileUrl) {
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(String.valueOf(chatId));
        sendDocument.setCaption("Информация по вашему вопросу сожержится в файле");
        sendDocument.setDocument(new InputFile(fileUrl));
        try {
            execute(sendDocument);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }

    /**
     * Метод отправляет пользователю сообщение
     *
     * @param chatId     идентификатор пользователя
     * @param textToSend текст сообщения
     * @throws TelegramApiException
     */
    public void sendMessage(long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        sendMessage.setParseMode(ParseMode.HTML);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }

    /**
     * Метод отправляет сообщение c клавиатурой
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
     * Метод отправляет сообщение с InlineKeyboard<br>
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
     * Метод создает InlineKeyboard
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
     * Метод создает клавиатуру внизу экрана
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
     * Метод создает клавиатуру для выбора приюта внизу экрана
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
     * Метод находит и удаляет последний запрос волонтеру от пользователя по chatId пользователя
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
     * Метод пересылает волонтеру сообщение с messageId от пользователя с chatId пользователя, позвавшего волонтера
     *
     * @param chatId    идентификатор чата пользователя, который позвал волонтера и написал сообщение волонтеру
     * @param messageId идентификатор пересылаемого волонтеру сообщения
     */
    private void forwardMessageToVolunteer(long chatId, int messageId) {
        ForwardMessage forwardMessage = new ForwardMessage(String.valueOf(config.getVolunteerChatId()), String.valueOf(chatId), messageId);
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
        } else if (config.getVolunteerChatId() == chatId
                // Если сообщение поступило от волонтера и содержит Reply на другое сообщение и текст в
                // Reply совпадает с тем что в мапе, то это сообщение отправляем юзеру
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
     * Метод изменяет состояние поля isDog - состояние выбранного приюта у Adopter
     * Используются методы:<br>
     *
     * @param chatId идентификатор чата пользователя, который выбрал/сменил приют
     * @param isDog  состояние выбранного приюта у Adopter
     */
    public void changeUserStatusOfShelter(Long chatId, boolean isDog) {
        AdopterDog adopterDog = adopterDogRepository.findAdopterDogByChatId(chatId);
        AdopterCat adopterCat = adopterCatRepository.findAdopterCatByChatId(chatId);
        if (isDog) {
            if (adopterDog == null) {
                adopterDog = new AdopterDog();
                adopterDog.setChatId(chatId);
            }
            adopterDog.setDog(true);
            adopterDogRepository.save(adopterDog);
            if (adopterCat != null) {
                adopterCat.setDog(true);
                adopterCatRepository.save(adopterCat);
            }
        } else {
            if (adopterCat == null) {
                adopterCat = new AdopterCat();
                adopterCat.setChatId(chatId);
            }
            adopterCat.setDog(false);
            adopterCatRepository.save(adopterCat);
            if (adopterDog != null) {
                adopterDog.setDog(false);
                adopterDogRepository.save(adopterDog);
            }
        }
    }

    /**
     * Создает клавиатуру и отсылает сообщение с ней для получения контактных данных пользователя
     *
     * @param chatId идентификатор чата пользователя
     */
    public void sendMessageWithContactKeyboard(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        KeyboardRow keyboardRow1 = new KeyboardRow();
        KeyboardButton contact = new KeyboardButton(SEND_CONTACT_CMD);
        contact.setRequestContact(true);
        keyboardRow1.add(contact);

        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(BACK_CMD);

        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(keyboardRow1);
        keyboard.add(keyboardRow2);

        replyKeyboardMarkup.setKeyboard(keyboard);

        sendMessage(chatId, MESSAGE_TEXT_SEND_CONTACT_CHOOSE, replyKeyboardMarkup);
    }

    /**
     * Обрабатывает присланные пользователем контактные данные и записывает их базу данных
     *
     * @param update принятый контакт пользователя
     */
    private void processContact(Update update) {
        User user = update.getMessage().getFrom();
        long chatId = update.getMessage().getChatId();
        if (adopterCatRepository.findAdopterCatByChatId(chatId) != null
                && !adopterCatRepository.findAdopterCatByChatId(chatId).isDog()) {
            AdopterCat adopterCat = adopterCatRepository.findAdopterCatByChatId(chatId);
            adopterCat.setChatId(user.getId());
            adopterCat.setFirstName(user.getFirstName());
            adopterCat.setLastName(user.getLastName());
            adopterCat.setUserName(user.getUserName());
            adopterCat.setTelephoneNumber(update.getMessage().getContact().getPhoneNumber());
            adopterCat.setState(REGISTRATION);
            adopterCatRepository.save(adopterCat);
        } else if (adopterDogRepository.findAdopterDogByChatId(chatId) != null
                && adopterDogRepository.findAdopterDogByChatId(chatId).isDog()) {
            AdopterDog adopterDog = adopterDogRepository.findAdopterDogByChatId(chatId);
            adopterDog.setChatId(user.getId());
            adopterDog.setFirstName(user.getFirstName());
            adopterDog.setLastName(user.getLastName());
            adopterDog.setUserName(user.getUserName());
            adopterDog.setTelephoneNumber(update.getMessage().getContact().getPhoneNumber());
            adopterDog.setState(REGISTRATION);
            adopterDogRepository.save(adopterDog);
        }
    }

    /**
     * Метод организует по расписанию автоматическую проверку наличия отчетов по сроку регистрации
     * по следующему алгоритму:<br>
     * - получение списка Adopter со статусом PROBATION, ADDITIONAL_PERIOD_14, ADDITIONAL_PERIOD_30;<br>
     * - получение для каждого Adopter из списка его отчетов последний отчет со статусом ACCEPTED или UNCHECKED;<br>
     * - при отсуствии отчета генерируется отчет (без регистрации в БД) для фиксации StatusDate;<br>
     * - отправка сообщения-уведомления волонтеру по итогу проверки на разницу 2 дня года между
     * текущей датой и дня года даты регистрации отчета со статусом ACCEPTED.<br>
     * - отправка сообщения-напоминания волонтеру по итогу проверки на разницу 1 день года между
     * текущей датой и дня года даты регистрации отчета со статусом UNCHEKED.<br>
     * - отправка сообщения-напоминания Adopter`у по итогу проверки на разницу 1 день года между
     * текущей датой и дня года даты регистрации отчета со статусом ACCEPTED.<br>
     * Аннотация @Scheduled с параметром (cron = "* * * * * *") актвирует метод по расписанию в момент,
     * указанный в параметре cron = "Секунда Минута Час День Месяц Год"
     *
     * @see Scheduled
     * @see ExaminationStatus
     * @see Status
     */
    //для проверки рабоспособности cron = "30 * * * * *"
    @Scheduled(cron = "30 30 8 * * *")
    protected void sendAttentionForDogVolunteerAndAdopterDog() {

        List<AdopterDog> adopters = adopterDogRepository.findAll();
        List<AdopterDog> adoptersWithProbationPeriod = adopters.stream()
                .filter(x -> (x.getState() == PROBATION)
                        || x.getState() == ADDITIONAL_PERIOD_14
                        || x.getState() == ADDITIONAL_PERIOD_30)
                .toList();
        List<ReportDog> reports = reportDogRepository.findAll();
        for (AdopterDog adopter : adoptersWithProbationPeriod) {
            ReportDog report = reports.stream()
                    .filter(x -> (x.getAdopterDog().equals(adopter))
                            && (x.getExamination().equals(ExaminationStatus.ACCEPTED)
                            || x.getExamination() == ExaminationStatus.UNCHECKED))
                    .reduce((first, last) -> last)
                    .orElse(new ReportDog(0L,
                            adopter.getStatusDate(),
                            "",
                            "There aren`t reports",
                            ExaminationStatus.REJECTED));
            if (LocalDate.now().getDayOfYear() - report.getReportDate().getDayOfYear() >= 1) {
                //для проверки рабоспособности в условии ниже добавить +3 после LocalDate.now().getDayOfYear()
                if (report.getExamination().equals(ExaminationStatus.UNCHECKED)) {
                    sendMessage(config.getVolunteerChatId(), "Внимание! Необходимо проверить отчет у "
                            + adopter.getFirstName() + " " + adopter.getLastName() + " chatID: " + adopter.getChatId());
                } else if (LocalDate.now().getDayOfYear() - report.getReportDate().getDayOfYear() > 2) {
                    sendMessage(config.getVolunteerChatId(), "Внимание! Усыновитель " + adopter.getFirstName()
                            + " " + adopter.getLastName() + " уже больше 2 дней не присылает отчеты!");
                }
                sendMessage(adopter.getChatId(), MESSAGE_ATTENTION_REPORT);
            }
        }
    }

    /**
     * Метод организует по расписанию автоматическую проверку наличия отчетов по сроку регистрации
     * по следующему алгоритму:<br>
     * - получение списка Adopter со статусом PROBATION, ADDITIONAL_PERIOD_14, ADDITIONAL_PERIOD_30;<br>
     * - получение для каждого Adopter из списка его отчетов последний отчет со статусом ACCEPTED или UNCHECKED;<br>
     * - при отсуствии отчета генерируется отчет (без регистрации в БД) для фиксации StatusDate;<br>
     * - отправка сообщения-уведомления волонтеру по итогу проверки на разницу 2 дня года между
     * текущей датой и дня года даты регистрации отчета со статусом ACCEPTED.<br>
     * - отправка сообщения-напоминания волонтеру по итогу проверки на разницу 1 день года между
     * текущей датой и дня года даты регистрации отчета со статусом UNCHEKED.<br>
     * - отправка сообщения-напоминания Adopter`у по итогу проверки на разницу 1 день года между
     * текущей датой и дня года даты регистрации отчета со статусом ACCEPTED.<br>
     * Аннотация @Scheduled с параметром (cron = "* * * * * *") актвирует метод по расписанию в момент,
     * указанный в параметре cron = "Секунда Минута Час День Месяц Год"
     *
     * @see Scheduled
     * @see ExaminationStatus
     * @see Status
     */
    //для проверки рабоспособности cron = "30 * * * * *"
    @Scheduled(cron = "30 30 8 * * *")
    protected void sendAttentionForCatVolunteerAndAdopterCat() {

        List<AdopterCat> adoptersWithProbationPeriod = adopterCatRepository.findAll().stream()
                .filter(x -> (x.getState() == PROBATION)
                        || x.getState() == ADDITIONAL_PERIOD_14
                        || x.getState() == ADDITIONAL_PERIOD_30)
                .toList();
        List<ReportCat> reports = reportCatRepository.findAll();
        for (AdopterCat adopter : adoptersWithProbationPeriod) {
            ReportCat report = reports.stream()
                    .filter(x -> (x.getAdopterCat().equals(adopter))
                            && (x.getExamination().equals(ExaminationStatus.ACCEPTED)
                            || x.getExamination() == ExaminationStatus.UNCHECKED))
                    .reduce((first, last) -> last)
                    .orElse(new ReportCat(0L,
                            adopter.getStatusDate(),
                            "",
                            "There aren`t reports",
                            ExaminationStatus.REJECTED));
            //для проверки рабоспособности в условии ниже добавить +3 после LocalDate.now().getDayOfYear()
            if (LocalDate.now().getDayOfYear() - report.getReportDate().getDayOfYear() >= 1) {
                //для проверки рабоспособности в условии ниже добавить +3 после LocalDate.now().getDayOfYear()
                if (report.getExamination().equals(ExaminationStatus.UNCHECKED)) {
                    sendMessage(config.getVolunteerChatId(), "Внимание! Необходимо проверить отчет у "
                            + adopter.getFirstName() + " " + adopter.getLastName() + " chatID: " + adopter.getChatId());
                } else if (LocalDate.now().getDayOfYear() - report.getReportDate().getDayOfYear() > 2) {
                    sendMessage(config.getVolunteerChatId(), "Внимание! Усыновитель " + adopter.getFirstName()
                            + " " + adopter.getLastName() + " уже больше 2 дней не присылает отчеты!");
                }
                sendMessage(adopter.getChatId(), MESSAGE_ATTENTION_REPORT);
            }
        }
    }

    /**
     * Метод организует по расписанию автоматическую проверку наличия отчетов со сроком регистрации превышающим:<br>
     * 30 дней для усыновителей со статусами PROBATION или ADDITIONAL_PERIOD_30;<br>
     * 14 дней для усыновителей со статусом ADDITIONAL_PERIOD_30.<br>
     * Согласно выбранному списку усыновителей бот осуществляет отправку волонтеру сообщения с текстом<br>
     * "{@value BotCommands#TAKE_DECISION} userName" и с кнопками  для выбора действия для каждого усыновителя<br>
     * Аннотация @Scheduled с параметром (cron = "* * * * * *") актвирует метод по расписанию cron = "Секунда Минута Час День Месяц Год"
     *
     * @see Status
     * @see Scheduled
     * @see Bot#sendMessageWithInlineKeyboard(long, String, String...)
     * @see BotCommands#KEYBOARD_DECISION
     */

    //для проверки рабоспособности cron = "30 * * * * *"
    @Scheduled(cron = "30 30 8 * * *")
    public void sendFinishListForDogVolunteer() {
        List<AdopterDog> adoptersWithFinishProbationPeriod = adopterDogRepository.findAll().stream()
                .filter(x -> ((x.getState() == PROBATION || x.getState() == ADDITIONAL_PERIOD_30)
                        //для проверки рабоспособности в условии ниже добавить +31 после LocalDate.now().getDayOfYear()
                        && (LocalDate.now().getDayOfYear() - x.getStatusDate().getDayOfYear() > 30))
                        || (x.getState() == ADDITIONAL_PERIOD_14
                        //для проверки рабоспособности в условии ниже добавить +15 после LocalDate.now().getDayOfYear()
                        && (LocalDate.now().getDayOfYear() - x.getStatusDate().getDayOfYear()) > 14))
                .toList();
        for (AdopterDog adopter : adoptersWithFinishProbationPeriod) {
            sendMessageWithInlineKeyboard(config.getVolunteerChatId(),
                    TAKE_DECISION + adopter.getUserName(),
                    KEYBOARD_DECISION);
        }
    }

    /**
     * Метод организует по расписанию автоматическую проверку наличия отчетов со сроком регистрации превышающим:<br>
     * 30 дней для усыновителей со статусами PROBATION или ADDITIONAL_PERIOD_30;<br>
     * 14 дней для усыновителей со статусом ADDITIONAL_PERIOD_30  :<br>
     * Аннотация @Scheduled с параметром (cron = "* * * * * *") актвирует метод по расписанию cron = "Секунда Минута Час День Месяц Год"
     *
     * @see Status
     * @see Scheduled
     */
    //для проверки рабоспособности cron = "30 * * * * *"
    @Scheduled(cron = "30 30 8 * * *")
    void sendFinishListForCatVolunteer() {
        List<AdopterCat> adoptersWithFinishProbationPeriod = adopterCatRepository.findAll().stream()
                .filter(x -> (x.getState() == PROBATION || x.getState() == ADDITIONAL_PERIOD_30)
                        && (LocalDate.now().getDayOfYear() - x.getStatusDate().getDayOfYear() + 30 > 30)
                        || (x.getState() == ADDITIONAL_PERIOD_14
                        && LocalDate.now().getDayOfYear() - x.getStatusDate().getDayOfYear() + 30 > 14))
                .toList();
        for (AdopterCat adopter : adoptersWithFinishProbationPeriod) {
            sendMessageWithInlineKeyboard(config.getVolunteerChatId(), TAKE_DECISION
                    + adopter.getUserName(), KEYBOARD_DECISION);

        }
    }

    /**
     * Метод разбивает текст сообщения, направленного волонтеру на 2 составляющих, получает userName, находит chatId для
     * данного пользователя и обновляет у него статус и дату статуса на дату обращения к методу <br>
     *
     * @param botReplies  сообщение уведомление для пользователя об изменении статуса
     * @param messageText текст сообщения из которого волонтер нажал кнопку в формате<br>
     *                    "{@value BotCommands#TAKE_DECISION} userName" для получения userName
     * @param status      значение статуса на который будет замена
     * @see AdopterDog#setState(Status)
     * @see AdopterDog#setStatusDate(LocalDate)
     */
    public void changeDogAdopterStatus(String botReplies, String messageText, Status status) {

        String userName = messageText.split(": ")[1];
        Long chatId = adopterDogRepository.findAll()
                .stream()
                .filter(x -> x.getUserName().equals(userName))
                .findFirst()
                .get()
                .getChatId();
        AdopterDog adopterDog = adopterDogService.get(chatId);
        adopterDog.setState(status);
        //для тестирования изменения даты использовать параметр LocalDate.now().minusDays(5)
        adopterDog.setStatusDate(LocalDate.now());
        sendMessage(chatId, botReplies);
        adopterDogRepository.save(adopterDog);
        adopterDogService.update(adopterDog);
        sendMessage(config.getVolunteerChatId(), "Для пользователя" + chatId + "выполнено:" + botReplies);
    }

    /**
     * Метод разбивает текст сообщения, направленного волонтеру на 2 составляющих, получает userName, находит chatId для
     * данного пользователя и обновляет у него статус и дату статуса на дату обращения к методу <br>
     *
     * @param botReplies  сообщение уведомление для пользователя об изменении статуса
     * @param messageText текст сообщения из которого волонтер нажал кнопку в формате<br>
     *                    "{@value BotCommands#TAKE_DECISION} userName" для получения userName
     * @param status      значение статуса на который будет замена
     * @see AdopterCat#setState(Status)
     * @see AdopterCat#setStatusDate(LocalDate)
     */
    void changeCatAdopterStatus(String botReplies, String messageText, Status status) {

        String userName = messageText.split(": ")[1];
        Long chatId = adopterCatRepository.findAll()
                .stream()
                .filter(x -> x.getUserName().equals(userName))
                .findFirst()
                .get()
                .getChatId();
        AdopterCat adopterCat = adopterCatService.get(chatId);
        adopterCat.setState(status);
        //для тестирования изменения даты использовать параметр LocalDate.now().minusDays(5)
        adopterCat.setStatusDate(LocalDate.now());
        sendMessage(chatId, botReplies);
        adopterCatRepository.save(adopterCat);
        adopterCatService.update(adopterCat);
        sendMessage(config.getVolunteerChatId(), "Для пользователя" + chatId + "выполнено:" + botReplies);
    }
}
