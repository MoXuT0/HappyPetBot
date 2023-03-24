package com.team4.happydogbot.service;

import com.team4.happydogbot.config.BotConfig;
import com.team4.happydogbot.entity.AdopterCat;
import com.team4.happydogbot.entity.AdopterDog;
import com.team4.happydogbot.entity.ReportCat;
import com.team4.happydogbot.entity.ReportDog;
import com.team4.happydogbot.repository.AdopterCatRepository;
import com.team4.happydogbot.repository.AdopterDogRepository;
import com.team4.happydogbot.replies.Reply;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.util.*;

import static com.team4.happydogbot.constants.BotCommands.*;
import static com.team4.happydogbot.constants.BotReplies.*;
import static com.team4.happydogbot.constants.PatternValidation.REPORT_REGEX;
import static com.team4.happydogbot.constants.PatternValidation.validationPatternReport;

@Slf4j
@Service
public class Bot extends TelegramLongPollingBot {
    private final BotConfig config;

    private final AdopterDogRepository adopterDogRepository;

    private final AdopterCatRepository adopterCatRepository;

    @Autowired
    public Bot(BotConfig config,
               AdopterDogRepository adopterDogRepository,
               AdopterCatRepository adopterCatRepository) {
        this.config = config;
        this.adopterDogRepository = adopterDogRepository;
        this.adopterCatRepository = adopterCatRepository;
    }

    public static final HashMap<String, Long> REQUEST_FROM_USER = new HashMap<>();

    public static final HashSet<Long> REQUEST_GET_REPLY_FROM_USER = new HashSet<>();

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
            } else if (SEND_CONTACT_CMD.equals(messageData) &&
                    adopterDogRepository.findAdopterDogByChatId(chatId).isDog()) {
                //МЕТОД ОТПРАВКИ КОНТАКТНЫХ ДАННЫХ в таблицу для собак
            } else if (SEND_CONTACT_CMD.equals(messageData)) {
                //МЕТОД ОТПРАВКИ КОНТАКТНЫХ ДАННЫХ в таблицу для кошек
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
}
