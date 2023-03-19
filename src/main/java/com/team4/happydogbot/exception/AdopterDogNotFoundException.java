package com.team4.happydogbot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AdopterDogNotFoundException extends RuntimeException {

    public AdopterDogNotFoundException() {
        super("AdopterDog is not found!");
    }
}
