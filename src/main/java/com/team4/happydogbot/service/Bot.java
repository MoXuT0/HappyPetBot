package com.team4.happydogbot.service;

import com.team4.happydogbot.config.BotConfig;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.team4.happydogbot.constants.Constants.*;

@Slf4j
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

    /* Принимает команду пользователя, отправляет ответ */
    @SneakyThrows
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
                    // Создаем мапу и кладем в нее сообщение в кач-ве ключа и chatId в кач-ве значения того кто позвал волонтера,
                    // то есть пока в мапе лежит текст и chatId - это значит что юзер находится в состоянии разговора с волонтером,
                    // отправляем сообщение пользователю
                    REQUEST_FROM_USER.put(messageText, chatId);
                    sendMessageWithInlineKeyboard(chatId,WRITE_VOLUNTEER, FINISH_VOLUNTEER);
                    break;
                default:
                    // Если в мапе уже есть chatId того кто написал боту, то есть продолжается общение с волонтером,
                    // то удаляем предыдущее сообщение и записываем новое сообщение, отправляем сообщение волонтеру
                    if (REQUEST_FROM_USER.containsValue(chatId)) {
                        findAndRemoveRequestFromUser(chatId);
                        REQUEST_FROM_USER.put(messageText, chatId);
                        forwardMessageToVolunteer(chatId, update.getMessage().getMessageId());
                    } else if (VOLUNTEER_ID == chatId
                            // Если сообщение поступило от волонтера и содержит Reply на другое сообщение и текст в
                            // Reply совпадает с тем что в мапе,
                            // то это сообщение отправляем юзеру
                            && update.getMessage().getReplyToMessage() != null
                            && REQUEST_FROM_USER.containsKey(update.getMessage().getReplyToMessage().getText())) {
                        String s = update.getMessage().getReplyToMessage().getText();
                        sendMessageToUser(REQUEST_FROM_USER.get(s), update.getMessage().getChat().getFirstName(), messageText);
                    } else {
                        // Если сообщение не подходит не под одну команду и волонтер и юзер на неходятся в состоянии
                        // разговора то выводим сообщение нет такой команды
                        sendMessage(chatId, NO_SUCH_COMMAND, null);
                    }
                    break;
            }

        } else if (update.hasCallbackQuery()) {
            String messageData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
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
                case FINISH_VOLUNTEER:
                    // Если юзер нажал кнопку Закончить разговор с волонтером, то удаляем последнее сообщение из мапы -
                    // т е выходим из состояния разговора с волонтером, заменяем сообщение на сообщение что разговор с
                    // волонтером закончен
                    findAndRemoveRequestFromUser(chatId);
                    executeEditMessageText(chatId, messageId);
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

    /**
     * Находит и удаляет последний запрос волонтеру от пользователя по chatId пользователя
     * @param chatId - идентификато чата пользователя, который позвал волонтера и написал сообщение волонтеру
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
     * @param chatId - идентификато чата пользователя, который позвал волонтера и написал сообщение волонтеру
     * @param messageId - идентефикатор пересылаемого волонтеру сообщения
     */
    private void forwardMessageToVolunteer(long chatId, int messageId) {
        ForwardMessage forwardMessage = new ForwardMessage(String.valueOf(VOLUNTEER_ID), String.valueOf(chatId), messageId);
        try {
            execute(forwardMessage);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }

    /**
     * Отправляет сообщение messageText от волонтера с имененм name пользователю с chatId, позвавшему волонтера
     * @param chatId - идентификато чата пользователя, который позвал волонтера, написал сообщение волонтеру и которому
     *               отправляется ответ волонтера.<br>
     * Используются методы:<br>
     * {@link #sendMessage(long chatId, String textToSend, ReplyKeyboard keyboard)}
     * {@link #sendMessageWithInlineKeyboard(long chatId, String textToSend, String... buttons)}
     * @param messageText - текст сообщения (ответа), который отправляется пользователю от волонтера
     * @param name - имя волонтера, который отправил сообщение (ответ) пользователю
     */
    private void sendMessageToUser(long chatId, String messageText, String name) {
        sendMessage(chatId, "Сообщение от волонтера " + messageText + ":\n" + name, null);
        sendMessageWithInlineKeyboard(chatId,WRITE_VOLUNTEER, FINISH_VOLUNTEER);
    }

    /**
     * Изменяет текст существующего сообщения, обычно после нажатия пользователем кнопки InlineKeyboardMaker
     * @param chatId - идентификато чата пользователя, в котором произошло действие и происходит изменение текста сообщения
     * @param messageId - идентефикатор сообщения, которое изменяется
     */
    private void executeEditMessageText(long chatId, long messageId) {
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setText(TALK_ENDED);
        message.setMessageId((int) messageId);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }
}
