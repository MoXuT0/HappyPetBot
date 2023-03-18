package com.team4.happydogbot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ReportDogNotFoundException extends RuntimeException {

    public ReportDogNotFoundException() {
        super("Отчет по собакам не найден!");
    }
}