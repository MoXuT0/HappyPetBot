package com.team4.happydogbot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AdopterDogNotFoundException extends RuntimeException {

    public AdopterDogNotFoundException() {
        super("Усыновитель собак не найден!");
    }
}
