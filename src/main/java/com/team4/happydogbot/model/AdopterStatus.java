package com.team4.happydogbot.model;

public enum AdopterStatus {

    NEW_CLIENT("Новый клиент"),
    POTENTIAL_ADOPTER("Потенциальный усыновитель"),
    ADOPTER("Усыновитель");

    private final String status;

    AdopterStatus(String status) {
        this.status = status;
    }

}
