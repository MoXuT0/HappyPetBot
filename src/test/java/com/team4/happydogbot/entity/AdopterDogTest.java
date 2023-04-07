package com.team4.happydogbot.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Тест - класс для проверки создания усыновителя собаки
 * @see AdopterDog
 * @see AdopterDogTest
 */
public class AdopterDogTest {
    private final Long chatId = 1234567890L;
    private final String firstName = "Ivan";
    private final String lastName = "Ivanov";
    private final String userName = "iiivanov56";
    private final int age = 35;
    private final String address = "Test";
    private final String telephoneNumber = "79516532288";
    private final Status state = Status.REGISTRATION;

    AdopterDog adopterDog = new AdopterDog(chatId, firstName, lastName, userName, age, address, telephoneNumber, state);

    @Test
    @DisplayName("Проверка на наличие данных при создании усыновителя собаки")
    public void createAdopterDogTest() {
        Long chatIdAD = adopterDog.getChatId();
        String firstNameAD = adopterDog.getFirstName();
        String lastNameAD = adopterDog.getLastName();
        String userNameAD = adopterDog.getUserName();
        int ageAD = adopterDog.getAge();
        String addressAD = adopterDog.getAddress();
        String telephoneNumberAD = adopterDog.getTelephoneNumber();
        Status stateAD = adopterDog.getState();

        Assertions.assertEquals(chatId, chatIdAD);
        Assertions.assertEquals(firstName, firstNameAD);
        Assertions.assertEquals(lastName, lastNameAD);
        Assertions.assertEquals(userName, userNameAD);
        Assertions.assertEquals(age, ageAD);
        Assertions.assertEquals(address, addressAD);
        Assertions.assertEquals(telephoneNumber, telephoneNumberAD);
        Assertions.assertEquals(state, stateAD);
    }

    @Test
    @DisplayName("Проверка на отсутствие переданных данных при создании усыновителя собаки")
    public void createAdopterDogNullTest() {
        AdopterDog adopterDogTest = new AdopterDog();
        Long chatIdAD = adopterDogTest.getChatId();
        String firstNameAD = adopterDogTest.getFirstName();
        String lastNameAD = adopterDogTest.getLastName();
        String userNameAD = adopterDogTest.getUserName();
        int ageAD = adopterDogTest.getAge();
        int ageActual = 0;
        String addressAD = adopterDogTest.getAddress();
        String telephoneNumberAD = adopterDogTest.getTelephoneNumber();
        Status stateAD = adopterDogTest.getState();
        Status statusActual = Status.USER;

        Assertions.assertNull(chatIdAD);
        Assertions.assertNull(firstNameAD);
        Assertions.assertNull(lastNameAD);
        Assertions.assertNull(userNameAD);
        Assertions.assertEquals(ageAD, ageActual);
        Assertions.assertNull(addressAD);
        Assertions.assertNull(telephoneNumberAD);
        Assertions.assertEquals(stateAD, statusActual);
    }
}
