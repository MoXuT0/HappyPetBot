package com.team4.happydogbot.replies;

import com.team4.happydogbot.service.Bot;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static com.team4.happydogbot.constants.BotCommands.*;
import static com.team4.happydogbot.constants.BotReplies.*;


public class Reply {

    private Bot bot;

    public Reply(Bot bot) {
        this.bot = bot;
    }
    /**
     * В мапе ключ - это команда, значение - реакция на команду
     * В этой мапе реакции на команду, если пользователь выбрал кошку
     * */
    public Map<String, Consumer<Long>> catReplies = new HashMap<>();
    {

        //Этап 0
        catReplies.put(PET_INFO_CMD, chatId -> bot.sendMessageWithInlineKeyboard(chatId, MESSAGE_TEXT_PET_INFO, KEYBOARD_CAT_ADOPT));

        //Этап 1
        catReplies.put(SHELTER_ABOUT_CMD, chatId -> bot.sendMessage(chatId, MESSAGE_CAT_SHELTER_ABOUT));
        catReplies.put(SHELTER_SAFETY_CMD, chatId -> bot.sendMessage(chatId, MESSAGE_CAT_SHELTER_SAFETY));
        catReplies.put(SHELTER_SCHEDULE_ADDRESS_CMD, chatId -> bot.sendMessage(chatId, MESSAGE_CAT_SHELTER_SCHEDULE_ADDRESS));
        catReplies.put(CAR_PASS_CMD, chatId -> bot.sendMessage(chatId, MESSAGE_CAT_CAR_PASS));
        catReplies.put(SEND_CONTACT_CMD, chatId -> bot.sendMessage(chatId, MESSAGE_CAT_SEND_CONTACT));

        //Этап 2
        catReplies.put(PET_RULES_CMD, chatId -> bot.sendMessage(chatId, MESSAGE_CAT_RULES));
        catReplies.put(PET_DOCS_CMD, chatId -> bot.sendMessage(chatId, MESSAGE_CAT_DOCS));
        catReplies.put(PET_TRANSPORT_CMD, chatId -> bot.sendMessage(chatId, MESSAGE_CAT_TRANSPORT));
        catReplies.put(PET_REFUSAL_CMD, chatId -> bot.sendMessage(chatId, MESSAGE_CAT_REFUSAL));
        catReplies.put(PET_HOUSE_CMD, chatId -> bot.sendMessageWithInlineKeyboard(chatId, MESSAGE_CAT_HOUSE_CHOOSE, KEYBOARD_CAT_HOUSE));

        catReplies.put(PET_HOUSE_FOR_KITTY_CMD, chatId -> bot.sendDocument(chatId, URL_CAT_HOUSE_KITTY));
        catReplies.put(PET_HOUSE_FOR_ADULT_CMD, chatId -> bot.sendDocument(chatId, URL_CAT_HOUSE_ADULT));
        catReplies.put(PET_HOUSE_FOR_SICK_CMD, chatId -> bot.sendDocument(chatId, URL_CAT_HOUSE_SICK));

    }

    /**
     * В мапе ключ - это команда, значение - реакция на команду
     * В этой мапе реакции на команду, если пользователь выбрал собаку,
     * и реакции, которые не зависят от выбора собака или кошка
     */
    public Map<String, Consumer<Long>> dogReplies = new HashMap<>();
    {
        //Этап 0
        dogReplies.put(SHELTER_CHOOSE, chatId -> bot.sendMessage(chatId, MESSAGE_TEXT_CHOOSE_SHELTER, bot.replyKeyboardShelter()));
        dogReplies.put(SHELTER_CAT, chatId -> {
            bot.sendMessage(chatId, MESSAGE_TEXT_CHOOSE_ACTION, bot.replyKeyboardBottom());
            bot.isDog = false;
        });
        dogReplies.put(SHELTER_DOG, chatId -> {
            bot.sendMessage(chatId, MESSAGE_TEXT_CHOOSE_ACTION, bot.replyKeyboardBottom());
            bot.isDog = true;
        });
        dogReplies.put(SHELTER_INFO_CMD, chatId -> bot.sendMessageWithInlineKeyboard(chatId, MESSAGE_TEXT_SHELTER_INFO, KEYBOARD_SHELTER_ABOUT));
        dogReplies.put(PET_INFO_CMD, chatId -> bot.sendMessageWithInlineKeyboard(chatId, MESSAGE_TEXT_PET_INFO, KEYBOARD_DOG_ADOPT));
        dogReplies.put(SEND_REPORT_CMD, chatId -> bot.sendMessageWithInlineKeyboard(chatId, MESSAGE_TEXT_REPORT, KEYBOARD_REPORT));
        dogReplies.put(FINISH_VOLUNTEER_CMD, chatId -> {
            bot.findAndRemoveRequestFromUser(chatId);
            bot.sendMessage(chatId, MESSAGE_TEXT_TALK_ENDED);
        });
        // Если юзер нажал кнопку Закончить разговор с волонтером,
        // то удаляем последнее сообщение из мапы - т.е. выходим из состояния разговора с волонтером,
        // выводим сообщение, что разговор с волонтером закончен

        //Этап 1
        dogReplies.put(SHELTER_ABOUT_CMD, chatId -> bot.sendMessage(chatId, MESSAGE_DOG_SHELTER_ABOUT));
        dogReplies.put(SHELTER_SAFETY_CMD, chatId -> bot.sendMessage(chatId, MESSAGE_DOG_SHELTER_SAFETY));
        dogReplies.put(SHELTER_SCHEDULE_ADDRESS_CMD, chatId -> bot.sendMessage(chatId, MESSAGE_DOG_SHELTER_SCHEDULE_ADDRESS));
        dogReplies.put(CAR_PASS_CMD, chatId -> bot.sendMessage(chatId, MESSAGE_DOG_CAR_PASS));
        dogReplies.put(SEND_CONTACT_CMD, chatId -> bot.sendMessage(chatId, MESSAGE_DOG_SEND_CONTACT));

        //Этап 2
        dogReplies.put(PET_RULES_CMD, chatId -> bot.sendMessage(chatId, MESSAGE_DOG_RULES));
        dogReplies.put(PET_DOCS_CMD, chatId -> bot.sendMessage(chatId, MESSAGE_DOG_DOCS));
        dogReplies.put(PET_TRANSPORT_CMD, chatId -> bot.sendMessage(chatId, MESSAGE_DOG_TRANSPORT));
        dogReplies.put(PET_ADVICES_CMD, chatId -> bot.sendMessage(chatId, MESSAGE_DOG_ADVICES));
        dogReplies.put(PET_CYNOLOGISTS_CMD, chatId -> bot.sendMessage(chatId, MESSAGE_DOG_CYNOLOGISTS));
        dogReplies.put(PET_REFUSAL_CMD, chatId -> bot.sendMessage(chatId, MESSAGE_DOG_REFUSAL));
        dogReplies.put(PET_HOUSE_CMD, chatId -> bot.sendMessageWithInlineKeyboard(chatId, MESSAGE_TEXT_DOG_HOUSE_CHOOSE, KEYBOARD_DOG_HOUSE));

        dogReplies.put(PET_HOUSE_FOR_PUPPY_CMD, chatId -> bot.sendDocument(chatId, URL_DOG_HOUSE_PUPPY));
        dogReplies.put(PET_HOUSE_FOR_ADULT_CMD, chatId -> bot.sendDocument(chatId, URL_DOG_HOUSE_ADULT));
        dogReplies.put(PET_HOUSE_FOR_SICK_CMD, chatId -> bot.sendDocument(chatId, URL_DOG_HOUSE_SICK));

        // Этап 3
        dogReplies.put(REPORT_FORM, chatId -> bot.sendMessage(chatId, MESSAGE_TEXT_REPORT_FORM));
        dogReplies.put(PET_HOUSE_CMD, chatId -> bot.sendMessageWithInlineKeyboard(257078762, TAKE_DECISION, KEYBOARD_DECISION));
    }

}
