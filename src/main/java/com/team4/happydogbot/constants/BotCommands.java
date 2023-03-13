package com.team4.happydogbot.constants;

public class BotCommands {

    // Команды этап 0. Входная точка общения бота с пользователем
    public static final String START_CMD = "/start";
    public static final String SHELTER_INFO_CMD = "Узнать информацию о приюте";
    public static final String PET_INFO_CMD = "Узнать как взять собаку из приюта";
    public static final String SEND_REPORT_CMD = "Прислать отчет о питомце";
    public static final String CALL_VOLUNTEER_CMD = "Позвать волонтера";
    public static final String FINISH_VOLUNTEER_CMD = "Закончить разговор с волонтером";

    // Команды этап 1. Вводная информация о приюте: где он находится, как и когда работает,
    // какие правила пропуска на территорию приюта, правила нахождения внутри и общения с собаками.
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

    // Команды этап 2. Помощь потенциальным «усыновителям» собаки из приюта
    // с бюрократическими (как оформить договор) и бытовыми (как подготовиться к жизни с собакой) вопросами.
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
    //Команды этапа 3. Ведение питомца
    public static final String REPORT_FORM = "Форма ежедневного отчета";
    public static final String SEND_REPORT  = "Отправить отчет";

    public static final String[] KEYBOARD_REPORT = {
            REPORT_FORM,
            SEND_REPORT
    };


}
