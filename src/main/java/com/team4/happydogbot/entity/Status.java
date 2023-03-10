package com.team4.happydogbot.entity;

/**
 * Class containing options for all stages of the user's state
 *
 */
public enum Status {

    REGISTRATION("Пользователь оставил свои контактные данные"),
    ADOPTION_DENIED("Потенциальному родителю откаазно в усыновлении животного"),
    PROBATION("Назначен испытательный срок - 30 календарных дней"),
    ADDITIONAL_PERIOD("Назначен дополнительный испытательный срок - 14 календарных дней");

    private final String status;

    Status(String status) {
        this.status = status;
    }
}
