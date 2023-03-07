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
                case "/start":
                    sendStartMessageWithReplyKeyboard(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "Узнать информацию о приюте":
                    sendMessageWithInlineKeyboard(chatId, MESSAGE_TEXT_ABOUT, KEYBOARD_ABOUT);
                    break;
                case "Узнать как взять собаку из приюта":
                    sendMessageWithInlineKeyboard(chatId, MESSAGE_TEXT_ADOPT, KEYBOARD_ADOPT);
                    break;
                case "Прислать отчет о питомце":
                    //Здесь будет метод для этапа 3
                    break;
                case "Позвать волонтера":
                    //Метод для вызова волонтера
                    break;
                default:
                    //Метод для вызова волонтера
                    break;
            }

        } else if (update.hasCallbackQuery()) {
            String messageData = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            switch (messageData) {
                case INFO_ABOUT:

                    break;
                case SCHEDULE_ADDRESS:

                    break;
                case SAFETY:

                    break;
                case SEND_CONTACT:

                    break;
                case RULES:

                    break;
                case DOCS:

                    break;
                case TRANSPORT:

                    break;
                case HOUSE:
                    sendMessageWithInlineKeyboard(chatId, MESSAGE_TEXT_HOUSE, KEYBOARD_HOUSE);
                    break;
                case HOUSE_FOR_PUPPY:

                    break;
                case HOUSE_FOR_ADULT:

                    break;
                case HOUSE_FOR_SICK:

                    break;

                case ADVICES:

                    break;
                case CYNOLOG:

                    break;
                case REFUSAL:

                    break;
                default:
                    //Метод для вызова волонтера
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
        keyboardRow1.add("Узнать информацию о приюте");
        keyboardRow1.add("Узнать как взять собаку из приюта");

        // Вторая строчка клавиатуры
        KeyboardRow keyboardRow2 = new KeyboardRow();
        // Добавляем кнопки во вторую строчку клавиатуры
        keyboardRow2.add("Прислать отчет о питомце");
        keyboardRow2.add("Позвать волонтера");

        // Добавляем все строчки клавиатуры в список
        keyboard.add(keyboardRow1);
        keyboard.add(keyboardRow2);

        // и устанавливаем этот список нашей клавиатуре
        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }

}
