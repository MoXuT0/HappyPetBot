package com.team4.happydogbot.constants;


import java.util.HashMap;

public class Constants {

    public static final String MESSAGE_TEXT_ABOUT = "Что именно вы хотите узнать о приюте?";
    //Команды этап 1
    public static final String INFO_ABOUT = "Общая информация о приюте";
    public static final String SCHEDULE_ADDRESS = "Расписание, адрес";
    public static final String SAFETY = "Техника безопасности";
    public static final String SEND_CONTACT = "Оправить свои контакты";

    public static final String[] KEYBOARD_ABOUT = {INFO_ABOUT, SCHEDULE_ADDRESS, SAFETY, SEND_CONTACT};

    public static final String MESSAGE_TEXT_ADOPT = "Что вас интересует?";

    //Команды этап 2
    public static final String RULES = "Правила знакомства с собакой";
    public static final String DOCS = "Необходимые документы";
    public static final String TRANSPORT = "Рекомендации по транспортировке";
    public static final String HOUSE = "Обустройство дома для собаки";
    public static final String ADVICES = "Первичное общение с собакой";
    public static final String CYNOLOG = "Проверенные кинологи";
    public static final String REFUSAL = "Причины отказа";

    public static final String[] KEYBOARD_ADOPT = {RULES, DOCS, TRANSPORT, HOUSE, ADVICES, CYNOLOG, REFUSAL};

    public static final String MESSAGE_TEXT_HOUSE = "О доме для какой собаки вы хотите узнать?";

    public static final String HOUSE_FOR_PUPPY = "Для щенка";
    public static final String HOUSE_FOR_ADULT = "Для взрослой собаки";
    public static final String HOUSE_FOR_SICK = "C ограниченными возможностями";

    public static final String[] KEYBOARD_HOUSE = {HOUSE_FOR_PUPPY, HOUSE_FOR_ADULT, HOUSE_FOR_SICK };

    public static final long VOLUNTEER_ID = 1607411391;

    public static final String FINISH_VOLUNTEER = "Закончить разговор с волонтером";

    public static final HashMap<String, Long> REQUEST_FROM_USER = new HashMap<>();

    public static final String WRITE_VOLUNTEER = "Напишите волонтеру:";

    public static final String TALK_ENDED = "Разговор с волонтером завершен";

    public static final String NO_SUCH_COMMAND = "Нет такой команды";
}
