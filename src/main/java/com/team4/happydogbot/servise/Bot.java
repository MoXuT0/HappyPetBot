package com.team4.happydogbot.servise;

import com.team4.happydogbot.config.BotConfig;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

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

/*Метод ответа на команды*/
    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            switch (messageText) {
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "Узнать информацию о приюте":
                     //Здесь будет метод для этапа 1
                    break;
                case "Узнать как взять собаку из приюта":
                    //Здесь будет метод для этапа 2

                    break;
                case "Прислать отчет о питомце":
                    //Здесь будет метод для этапа 3

                    break;
                case "Позвать волонтера":
                    //Метод для вызова волонтера
                    //  System.out.println(message.getText());
                    break;
                default:
                    //Метод для вызова волонтера
                    break;
            }
        }
    }
    /* Ответ на команду /start
     создает текст ответа,
     вызывает метод, отправляющий сообщение */
    private void startCommandReceived(long chatId, String name) {
        String startAnswer = name + ", выберите действие";
        sendStartMessage(chatId, startAnswer);
    }

    /* Отправляет сообщение,
       вызывает метод, создающий клавиатуру Этапа 0 */
    private void sendStartMessage(long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        sendMessage.setReplyMarkup(replyKeyboardMaker());
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
/* Создает клавиатуру Этапа 0 */
    private ReplyKeyboardMarkup replyKeyboardMaker(){
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
