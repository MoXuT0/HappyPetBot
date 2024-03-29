package com.team4.happydogbot.entity;

/**
 * Класс, содержащий параметры для всех стадий пользовательского состояния
 * @see AdopterCat
 * @see AdopterDog
 */
public enum Status {
    USER("Пользователь выбрал приют"),
    REGISTRATION("Пользователь оставил свои контактные данные"),
    ADOPTION_DENIED("Потенциальному родителю отказано в усыновлении животного"),
    PROBATION("Назначен испытательный срок - 30 календарных дней"),
    ADDITIONAL_PERIOD_14("Назначен дополнительный испытательный срок - 14 календарных дней"),
    //исправлено по обновленному ТЗ
    ADDITIONAL_PERIOD_30("Назначен дополнительный испытательный срок - 30 календарных дней"),
    //дополнено по обновленному ТЗ
    FINISHED_PROBATION_PERIOD("Испытательный срок пройден - работа с усыновителем закончена");

    private final String status;

    Status(String status) {
        this.status = status;
    }
}
