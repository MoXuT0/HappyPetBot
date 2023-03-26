package com.team4.happydogbot.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Тест - класс для проверки создания усыновителя кота
 * @see AdopterCat
 * @see AdopterCatTest
 */
public class AdopterCatTest {
    private final Long chatId = 1234567890L;
    private final String firstName = "Ivan";
    private final String lastName = "Ivanov";
    private final String userName = "iiivanov56";
    private final int age = 35;
    private final String address = "Test";
    private final String telephoneNumber = "79516532288";
    private final Status state = Status.REGISTRATION;

    AdopterCat adopterCat = new AdopterCat(chatId, firstName, lastName, userName, age, address, telephoneNumber,state);

    @Test
    @DisplayName("Проверка на наличие данных при создании усыновителя кота")
    public void createAdopterCatTest() {
        Long chatIdAC = adopterCat.getChatId();
        String firstNameAC = adopterCat.getFirstName();
        String lastNameAC = adopterCat.getLastName();
        String userNameAC = adopterCat.getUserName();
        int ageAC = adopterCat.getAge();
        String addressAC = adopterCat.getAddress();
        String telephoneNumberAC = adopterCat.getTelephoneNumber();
        Status stateAC = adopterCat.getState();

        Assertions.assertEquals(chatId, chatIdAC);
        Assertions.assertEquals(firstName, firstNameAC);
        Assertions.assertEquals(lastName, lastNameAC);
        Assertions.assertEquals(userName, userNameAC);
        Assertions.assertEquals(age, ageAC);
        Assertions.assertEquals(address, addressAC);
        Assertions.assertEquals(telephoneNumber, telephoneNumberAC);
        Assertions.assertEquals(state, stateAC);
    }

    @Test
    @DisplayName("Проверка на отсутствие переданных данных при создании усыновителя кота")
    public void createAdopterCatNullTest() {
        AdopterCat adopterCatTest = new AdopterCat();
        Long chatIdAC = adopterCatTest.getChatId();
        String firstNameAC = adopterCatTest.getFirstName();
        String lastNameAC = adopterCatTest.getLastName();
        String userNameAC = adopterCatTest.getUserName();
        int ageAC = adopterCatTest.getAge();
        int ageActual = 0;
        String addressAC = adopterCatTest.getAddress();
        String telephoneNumberAC = adopterCatTest.getTelephoneNumber();
        Status stateAC = adopterCatTest.getState();
        Status statusActual = Status.REGISTRATION;

        Assertions.assertNull(chatIdAC);
        Assertions.assertNull(firstNameAC);
        Assertions.assertNull(lastNameAC);
        Assertions.assertNull(userNameAC);
        Assertions.assertEquals(ageAC, ageActual);
        Assertions.assertNull(addressAC);
        Assertions.assertNull(telephoneNumberAC);
        Assertions.assertEquals(stateAC, statusActual);
    }
}
