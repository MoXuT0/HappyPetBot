package com.team4.happydogbot.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Тест - класс для проверки создания собаки
 * @see Dog
 * @see DogTest
 */
public class DogTest {
    private final Long id = 2L;
    private final String name = "Sharik";
    private final String breed = "Buldog";
    private final int yearOfBirth = 2018;
    private final String description = "Test2";

    Dog dogTest = new Dog(id, name, breed, yearOfBirth, description);

    @Test
    @DisplayName("Проверка на наличие данных при создании собаки")
    public void createDogTest() {
        Long dogId = dogTest.getId();
        String dogName = dogTest.getName();
        String dogBreed = dogTest.getBreed();
        int dogYearOfBirth = dogTest.getYearOfBirth();
        String dogDescription = dogTest.getDescription();

        Assertions.assertEquals(id, dogId);
        Assertions.assertEquals(name, dogName);
        Assertions.assertEquals(breed, dogBreed);
        Assertions.assertEquals(yearOfBirth, dogYearOfBirth);
        Assertions.assertEquals(description, dogDescription);
    }

    @Test
    @DisplayName("Проверка на отсутствие переданных данных при создании собаки")
    public void createDogNullTest() {
        Dog dogTest = new Dog();
        Long dogId = dogTest.getId();
        String dogName = dogTest.getName();
        String dogBreed = dogTest.getBreed();
        int dogYearOfBirth = dogTest.getYearOfBirth();
        int dogYearActual = 0;
        String dogDescription = dogTest.getDescription();

        Assertions.assertNull(dogId);
        Assertions.assertNull(dogName);
        Assertions.assertNull(dogBreed);
        Assertions.assertEquals(dogYearOfBirth, dogYearActual);
        Assertions.assertNull(dogDescription);
    }
}
