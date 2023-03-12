package com.team4.happydogbot.constants;


import java.util.HashMap;

public class Constants {

    // Команды этап 0
    public static final String START_CMD = "/start";
    public static final String SHELTER_INFO_CMD = "Узнать информацию о приюте";
    public static final String PET_INFO_CMD = "Узнать как взять собаку из приюта";
    public static final String SEND_REPORT_CMD = "Прислать отчет о питомце";
    public static final String CALL_VOLUNTEER_CMD = "Позвать волонтера";
    public static final String FINISH_VOLUNTEER_CMD = "Закончить разговор с волонтером";

    //Команды этап 1
    public static final String SHELTER_ABOUT_CMD = "Общая информация о приюте";
    public static final String SHELTER_SCHEDULE_ADDRESS_CMD = "Расписание, адрес";
    public static final String SHELTER_SAFETY_CMD = "Техника безопасности";
    public static final String SEND_CONTACT_CMD = "Отправить свои контакты";

    public static final String[] KEYBOARD_SHELTER_ABOUT = {
            SHELTER_ABOUT_CMD,
            SHELTER_SCHEDULE_ADDRESS_CMD,
            SHELTER_SAFETY_CMD,
            SEND_CONTACT_CMD
    };

    //Команды этап 2
    public static final String PET_RULES_CMD = "Правила знакомства с собакой";
    public static final String PET_DOCS_CMD = "Необходимые документы";
    public static final String PET_TRANSPORT_CMD = "Рекомендации по транспортировке";
    public static final String PET_HOUSE_CMD = "Обустройство дома для собаки";
    public static final String PET_ADVICES_CMD = "Первичное общение с собакой";
    public static final String PET_CYNOLOGISTS_CMD = "Проверенные кинологи";
    public static final String PET_REFUSAL_CMD = "Причины отказа";

    public static final String[] KEYBOARD_PET_ADOPT = {
            PET_RULES_CMD,
            PET_DOCS_CMD,
            PET_TRANSPORT_CMD,
            PET_HOUSE_CMD,
            PET_ADVICES_CMD,
            PET_CYNOLOGISTS_CMD,
            PET_REFUSAL_CMD,
            SEND_CONTACT_CMD
    };

    public static final String PET_HOUSE_FOR_PUPPY_CMD = "Для щенка";
    public static final String PET_HOUSE_FOR_ADULT_CMD = "Для взрослой собаки";
    public static final String PET_HOUSE_FOR_SICK_CMD = "C ограниченными возможностями";

    public static final String[] KEYBOARD_PET_HOUSE = {
            PET_HOUSE_FOR_PUPPY_CMD,
            PET_HOUSE_FOR_ADULT_CMD,
            PET_HOUSE_FOR_SICK_CMD
    };

    // Ответы этап 0
    public static final String MESSAGE_TEXT_GREETINGS = "ТЕКСТ ПРИВЕТСТВИЯ";
    public static final String MESSAGE_TEXT_NO_COMMAND = "Нет такой команды";
    public static final String MESSAGE_TEXT_WRITE_VOLUNTEER = "Напишите волонтеру:";
    public static final String MESSAGE_TEXT_TALK_ENDED = "Разговор с волонтером завершен";
    public static final String MESSAGE_TEXT_WAS_SENT = "Ваше сообщение отправлено, волонтер скоро ответит Вам";

    // Ответы этап 1
    public static final String MESSAGE_TEXT_SHELTER_INFO = "Что именно вы хотите узнать о приюте?";
    public static final String MESSAGE_TEXT_SHELTER_ABOUT = "ОБЩАЯ ИНФОРМАЦИЯ";
    public static final String MESSAGE_TEXT_SHELTER_SCHEDULE_ADDRESS = "РАСПИСАНИЕ, АДРЕСС";
    public static final String MESSAGE_TEXT_SHELTER_SAFETY = "ПРАВИЛА БЕЗОПАСНОСТИ";
    public static final String MESSAGE_TEXT_SEND_CONTACT = "ОТПРАВКА КОНТАКТНЫХ ДАННЫХ";

    // Ответы этап 2
    public static final String MESSAGE_TEXT_PET_INFO = "Что вас интересует?";
    public static final String MESSAGE_TEXT_PET_RULES = "ПРАВИЛА ЗНАКОМСТВА С СОБАКОЙ";
    public static final String MESSAGE_TEXT_PET_DOCS = "НЕОБХОДИМЫЕ ДОКУМЕНТЫ";
    public static final String MESSAGE_TEXT_PET_TRANSPORT = "РЕКОМЕНДАЦИИ ПО ТРАНСПОРТИРОВКЕ";
    public static final String MESSAGE_TEXT_PET_HOUSE_CHOOSE = "О доме для какой собаки вы хотите узнать?";
    public static final String MESSAGE_TEXT_PET_HOUSE_PUPPY = "ОБУСТРОЙСТВО ДОМА ДЛЯ ЩЕНКА";
    public static final String MESSAGE_TEXT_PET_HOUSE_ADULT = "ОБУСТРОЙСТВО ДОМА ДЛЯ ВЗРОСЛОЙ СОБАКИ";
    public static final String MESSAGE_TEXT_PET_HOUSE_SICK = "ОБУСТРОЙСТВО ДОМА ДЛЯ СОБАКИ С ОГРАНИЧЕННЫМИ ВОЗМОЖНОСТЯМИ";
    public static final String MESSAGE_TEXT_PET_ADVICES = "СОВЕТЫ КИНОЛОГА ПО ПЕРВИЧНОМУ ОБЩЕНИЮ";
    public static final String MESSAGE_TEXT_PET_CYNOLOGISTS = "РЕКОМЕНДАЦИИ ПО ПРОВЕРЕННЫМ КИНОЛОГАМИ";
    public static final String MESSAGE_TEXT_PET_REFUSAL = "ПРИЧИНЫ ОТКАЗА";

    // Константы для вызова волонтера
    public static final long VOLUNTEER_ID = 359859422;
    public static final HashMap<String, Long> REQUEST_FROM_USER = new HashMap<>();
}
