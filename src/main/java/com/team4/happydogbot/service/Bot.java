package com.team4.happydogbot.service;

import com.team4.happydogbot.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.team4.happydogbot.constants.BotCommands.*;
import static com.team4.happydogbot.constants.BotReplies.*;

@Slf4j
@Service
public class Bot extends TelegramLongPollingBot {
    final BotConfig config;

    public Bot(BotConfig config) {
        this.config = config;
    }

    public static final long VOLUNTEER_ID = 1607411391;
    public static final HashMap<String, Long> REQUEST_FROM_USER = new HashMap<>();

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
            switch (messageText) {
                case START_CMD:
                    sendMessage(chatId, MESSAGE_TEXT_GREETINGS);
                    sendStartMessageWithReplyKeyboard(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case SHELTER_INFO_CMD:
                    sendMessageWithInlineKeyboard(chatId, MESSAGE_TEXT_SHELTER_INFO, KEYBOARD_SHELTER_ABOUT);
                    break;
                case PET_INFO_CMD:
                    sendMessageWithInlineKeyboard(chatId, MESSAGE_TEXT_PET_INFO, KEYBOARD_PET_ADOPT);
                    break;
                case SEND_REPORT_CMD:
                    //Здесь будет метод для этапа 3
                    break;
                case CALL_VOLUNTEER_CMD:
                    // Создаем мапу и кладем в нее сообщение в кач-ве ключа и chatId в кач-ве значения того, кто позвал волонтера,
                    // то есть пока в мапе лежит текст и chatId - это значит что юзер находится в состоянии разговора с волонтером,
                    // отправляем сообщение пользователю
                    REQUEST_FROM_USER.put(messageText, chatId);
                    sendMessageWithInlineKeyboard(chatId, MESSAGE_TEXT_WRITE_VOLUNTEER, FINISH_VOLUNTEER_CMD);
                    break;
                default:
                    talkWithVolunteerOrNoSuchCommand(chatId, update);
                    break;
            }

        } else if (update.hasCallbackQuery()) {
            String messageData = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            switch (messageData) {
                case SHELTER_ABOUT_CMD:
                    sendMessage(chatId, MESSAGE_TEXT_SHELTER_ABOUT);
                    break;
                case SHELTER_SCHEDULE_ADDRESS_CMD:
                    sendMessage(chatId, MESSAGE_TEXT_SHELTER_SCHEDULE_ADDRESS);
                    break;
                case SHELTER_SAFETY_CMD:
                    sendMessage(chatId, MESSAGE_TEXT_SHELTER_SAFETY);
                    break;
                case PET_RULES_CMD:
                    sendMessage(chatId, MESSAGE_TEXT_PET_RULES);
                    break;
                case PET_DOCS_CMD:
                    sendMessage(chatId, MESSAGE_TEXT_PET_DOCS);
                    break;
                case PET_TRANSPORT_CMD:
                    sendMessage(chatId, MESSAGE_TEXT_PET_TRANSPORT);
                    break;
                case PET_HOUSE_CMD:
                    sendMessageWithInlineKeyboard(chatId, MESSAGE_TEXT_PET_HOUSE_CHOOSE, KEYBOARD_PET_HOUSE);
                    break;
                case PET_HOUSE_FOR_PUPPY_CMD:
                    sendMessage(chatId, MESSAGE_TEXT_PET_HOUSE_PUPPY);
                    break;
                case PET_HOUSE_FOR_ADULT_CMD:
                    sendMessage(chatId, MESSAGE_TEXT_PET_HOUSE_ADULT);
                    break;
                case PET_HOUSE_FOR_SICK_CMD:
                    sendMessage(chatId, MESSAGE_TEXT_PET_HOUSE_SICK);
                    break;
                case PET_ADVICES_CMD:
                    sendMessage(chatId, MESSAGE_TEXT_PET_ADVICES);
                    break;
                case PET_CYNOLOGISTS_CMD:
                    sendMessage(chatId, MESSAGE_TEXT_PET_CYNOLOGISTS);
                    break;
                case PET_REFUSAL_CMD:
                    sendMessage(chatId, MESSAGE_TEXT_PET_REFUSAL);
                    break;
                case SEND_CONTACT_CMD:
                    sendMessage(chatId, MESSAGE_TEXT_SEND_CONTACT);
                    // МЕТОД ОТПРАВКИ КОНТАКТНЫХ ДАННЫХ
                    break;
                case FINISH_VOLUNTEER_CMD:
                    // Если юзер нажал кнопку Закончить разговор с волонтером, то удаляем последнее сообщение из мапы -
                    // т е выходим из состояния разговора с волонтером, выводим сообщение, что разговор с волонтером закончен
                    findAndRemoveRequestFromUser(chatId);
                    sendMessage(chatId, MESSAGE_TEXT_TALK_ENDED);
                    break;
                default:
                    sendMessage(chatId, MESSAGE_TEXT_NO_COMMAND);
                    break;
            }
        }
    }

    /**
     * Отправляет сообщение
     * @param chatId идентификатор пользователя
     * @param textToSend текст сообщения
     * @throws TelegramApiException
     */
    private void sendMessage(long chatId, String textToSend) {
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
     * @param chatId идентификатор пользователя
     * @param textToSend текст сообщения
     * @param keyboard клавиатура
     * @throws TelegramApiException
     */

    private void sendMessage(long chatId, String textToSend, ReplyKeyboard keyboard) {
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
     * @param chatId идентификатор пользователя
     * @param textToSend текст сообщения
     * @param buttons множество (массив или varargs) кнопок клавиатуры
     */
    void sendMessageWithInlineKeyboard(long chatId, String textToSend, String... buttons) {
        InlineKeyboardMarkup inlineKeyboard = InlineKeyboardMaker(buttons);
        sendMessage(chatId, textToSend, inlineKeyboard);
    }

    /**
     * Отправляет сообщение и клавиатуру Этапа 0 по команде start
     * @param chatId идентификатор пользователя
     * @param name имя пользователя
     */
    void sendStartMessageWithReplyKeyboard(long chatId, String name) {
        String startAnswer = name + ", выберите действие";
        sendMessage(chatId, startAnswer, replyKeyboardMaker());
    }

    /**
     * Создает InlineKeyboard
     * @param buttons множество (массив или varargs) кнопок клавиатуры
     * @return клавиатура привязанная к сообщению
     */
    InlineKeyboardMarkup InlineKeyboardMaker(String... buttons) {
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
     * Создает клавиатуру Этапа 0
     * @return клавиатура с вариантами команд
     */
    private ReplyKeyboardMarkup replyKeyboardMaker() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
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

        // Добавляем все строчки клавиатуры в список
        keyboard.add(keyboardRow1);
        keyboard.add(keyboardRow2);

        // и устанавливаем этот список нашей клавиатуре
        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }

    /**
     * Находит и удаляет последний запрос волонтеру от пользователя по chatId пользователя
     *
     * @param chatId идентификатор чата пользователя, который позвал волонтера и написал сообщение волонтеру
     */
    private void findAndRemoveRequestFromUser(long chatId) {
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
}
