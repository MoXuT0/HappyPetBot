package com.team4.happydogbot.service;

import com.team4.happydogbot.config.BotConfig;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

import static com.team4.happydogbot.constants.Constants.*;

@Service
public class Bot extends TelegramLongPollingBot {
    final BotConfig config;

    public Bot(BotConfig config) {
        this.config = config;
    }

    @Override
    public String getBotUsername() {
        return config.getName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    /* Принимает коменду пользователя, отправляет ответ */
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            switch (messageText) {
                case START_CMD:
                    sendMessage(chatId, MESSAGE_TEXT_GREETINGS, null);
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
                    //Метод для вызова волонтера
                    break;
                default:
                    sendMessage(chatId, MESSAGE_TEXT_NO_COMMAND , null);
                    break;
            }

        } else if (update.hasCallbackQuery()) {
            String messageData = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            switch (messageData) {
                case SHELTER_ABOUT_CMD:
                    sendMessage(chatId, MESSAGE_TEXT_SHELTER_ABOUT, null);
                    break;
                case SHELTER_SCHEDULE_ADDRESS_CMD:
                    sendMessage(chatId, MESSAGE_TEXT_SHELTER_SCHEDULE_ADDRESS, null);
                    break;
                case SHELTER_SAFETY_CMD:
                    sendMessage(chatId, MESSAGE_TEXT_SHELTER_SAFETY, null);
                    break;
                case PET_RULES_CMD:
                    sendMessage(chatId, MESSAGE_TEXT_PET_RULES, null);
                    break;
                case PET_DOCS_CMD:
                    sendMessage(chatId, MESSAGE_TEXT_PET_DOCS, null);
                    break;
                case PET_TRANSPORT_CMD:
                    sendMessage(chatId, MESSAGE_TEXT_PET_TRANSPORT, null);
                    break;
                case PET_HOUSE_CMD:
                    sendMessageWithInlineKeyboard(chatId, MESSAGE_TEXT_PET_HOUSE_CHOOSE, KEYBOARD_PET_HOUSE);
                    break;
                case PET_HOUSE_FOR_PUPPY_CMD:
                    sendMessage(chatId, MESSAGE_TEXT_PET_HOUSE_PUPPY, null);
                    break;
                case PET_HOUSE_FOR_ADULT_CMD:
                    sendMessage(chatId, MESSAGE_TEXT_PET_HOUSE_ADULT, null);
                    break;
                case PET_HOUSE_FOR_SICK_CMD:
                    sendMessage(chatId, MESSAGE_TEXT_PET_HOUSE_SICK, null);
                    break;
                case PET_ADVICES_CMD:
                    sendMessage(chatId, MESSAGE_TEXT_PET_ADVICES, null);
                    break;
                case PET_CYNOLOGISTS_CMD:
                    sendMessage(chatId, MESSAGE_TEXT_PET_CYNOLOGISTS, null);
                    break;
                case PET_REFUSAL_CMD:
                    sendMessage(chatId, MESSAGE_TEXT_PET_REFUSAL, null);
                    break;
                case SEND_CONTACT_CMD:
                    sendMessage(chatId, MESSAGE_TEXT_SEND_CONTACT, null);
                    // МЕТОД ОТПРАВКИ КОНТАКТНЫХ ДАННЫХ
                    break;
                case CALL_VOLUNTEER_CMD:
                    sendMessage(chatId, MESSAGE_TEXT_CALL_VOLUNTEER, null);
                    //Метод для вызова волонтера
                    break;
                default:
                    sendMessage(chatId, MESSAGE_TEXT_NO_COMMAND , null);
                    break;
            }
        }
    }

    /* Отправляет ответ c клавиатурой, может и без, если ReplyKeyboard поставить null*/
    private void sendMessage(long chatId, String textToSend, ReplyKeyboard keyboard) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        sendMessage.setReplyMarkup(keyboard);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /* Отправляет ответ с  InlineKeyboard */
    void sendMessageWithInlineKeyboard(long chatId, String textToSend, String... buttons) {
        InlineKeyboardMarkup inlineKeyboard = InlineKeyboardMaker(buttons);
        sendMessage(chatId, textToSend, inlineKeyboard);
    }

    /* Отправляет ответ и клавиатуру Этапа 0 по команде start */
    void sendStartMessageWithReplyKeyboard(long chatId, String name) {
        String startAnswer = name + ", выберите действие";
        sendMessage(chatId, startAnswer, replyKeyboardMaker());
    }

    /* Создает InlineKeyboard, принимает набор команд */
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


    /* Создает клавиатуру Этапа 0 */
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

}
