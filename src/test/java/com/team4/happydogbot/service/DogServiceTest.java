package com.team4.happydogbot.service;

import com.team4.happydogbot.entity.Dog;
import com.team4.happydogbot.exception.DogNotFoundException;
import com.team4.happydogbot.repository.DogRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Тест - класс для проверки CRUD операций в классе - сервисе собаки
 * @see Dog
 * @see DogRepository
 * @see DogService
 * @see DogServiceTest
 */
@ExtendWith(MockitoExtension.class)
public class DogServiceTest {

    @Mock
    DogRepository dogRepository;

    @InjectMocks
    DogService dogService;

    private final Dog expected = new Dog();
    private final Dog expected1 = new Dog();

    @BeforeEach
    public void setUp() {
        expected.setId(1L);
        expected.setName("Bublik");
        expected.setBreed("Pudel");
        expected.setYearOfBirth(2020);
        expected.setDescription("Test");

        expected1.setId(2L);
        expected1.setName("Korjik");
        expected1.setBreed("Buldog");
        expected1.setYearOfBirth(2017);
        expected1.setDescription("Test");
    }

    /**
     * Тестирование метода <b>add()</b> в DogService
     * <br>
     * Mockito: когда вызывается метод <b>DogRepository::save</b>,
     * возвращается собака <b>expected</b>
     */
    @Test
    @DisplayName("Проверка добавления новой собаки и сохранения ее в базе данных")
    public void addDogTest() {
        when(dogRepository.save(any(Dog.class))).thenReturn(expected);

        Dog actual = dogService.add(expected);

        Assertions.assertThat(actual.getName()).isEqualTo(expected.getName());
        Assertions.assertThat(actual.getBreed()).isEqualTo(expected.getBreed());
        Assertions.assertThat(actual.getYearOfBirth()).isEqualTo(expected.getYearOfBirth());
        Assertions.assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
    }

    /**
     * Тестирование метода <b>get()</b> в DogService
     * <br>
     * Mockito: когда вызывается метод <b>DogRepository::findById</b>,
     * возвращается собака <b>expected</b>
     */
    @Test
    @DisplayName("Проверка поиска собаки по id и возвращения ее из базы данных")
    public void getByIdDogTest() {
        when(dogRepository.findById(any(Long.class))).thenReturn(Optional.of(expected));

        Dog actual = dogService.get(expected.getId());

        Assertions.assertThat(actual.getName()).isEqualTo(expected.getName());
        Assertions.assertThat(actual.getBreed()).isEqualTo(expected.getBreed());
        Assertions.assertThat(actual.getYearOfBirth()).isEqualTo(expected.getYearOfBirth());
        Assertions.assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
    }

    /**
     * Тест на создание исключения в методе <b>get()</b> в DogService
     * <br>
     * Mockito: когда вызывается метод <b>DogRepository::findById</b>,
     * выбрасывается исключение <b>DogNotFoundException</b>
     * @throws DogNotFoundException
     */
    @Test
    @DisplayName("Проверка выброса исключения в методе поиска собаки")
    public void getByIdDogExceptionTest() {
        when(dogRepository.findById(any(Long.class))).thenThrow(DogNotFoundException.class);
        org.junit.jupiter.api.Assertions.assertThrows(DogNotFoundException.class, () -> dogService.get(0L));
    }

    /**
     * Тестирование метода <b>update()</b> в DogService
     * <br>
     * Mockito: когда вызывается метод <b>DogRepository::findById</b> и <b>DogRepository::save</b>,
     * возвращается отредактированная собака <b>expected</b>
     */
    @Test
    @DisplayName("Проверка редактирования собаки, сохранения и возвращения ее из базы данных")
    public void updateDogTest() {
        when(dogRepository.findById(any(Long.class))).thenReturn(Optional.of(expected));
        when(dogRepository.save(any(Dog.class))).thenReturn(expected);

        Dog actual = dogService.update(expected);

        Assertions.assertThat(actual.getName()).isEqualTo(expected.getName());
        Assertions.assertThat(actual.getBreed()).isEqualTo(expected.getBreed());
        Assertions.assertThat(actual.getYearOfBirth()).isEqualTo(expected.getYearOfBirth());
        Assertions.assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
    }

    /**
     * Тест на создание исключения в методе <b>update()</b> в DogService
     * <br>
     * Mockito: когда вызывается метод <b>DogRepository::findById</b>,
     * выбрасывается исключение <b>DogNotFoundException</b>
     * @throws DogNotFoundException
     */
    @Test
    @DisplayName("Проверка выброса исключения в методе редактирования собаки")
    public void updateDogExceptionTest() {
        when(dogRepository.findById(any(Long.class))).thenThrow(DogNotFoundException.class);
        org.junit.jupiter.api.Assertions.assertThrows(DogNotFoundException.class,
                () -> dogService.update(expected));
    }

    /**
     * Тестирование метода <b>getAll()</b> в DogService
     * <br>
     * Mockito: когда вызывается метод <b>DogRepository::findAll</b>,
     * возвращается коллекция собак <b>adopterDogs</b>
     */
    @Test
    @DisplayName("Проверка поиска всех собак и возвращения их из базы данных")
    public void getAllDogsTest() {
        List<Dog> dogs = new ArrayList<>();
        dogs.add(expected);
        dogs.add(expected1);

        when(dogRepository.findAll()).thenReturn(dogs);

        Collection<Dog> actual = dogService.getAll();

        Assertions.assertThat(actual.size()).isEqualTo(dogs.size());
        Assertions.assertThat(actual).isEqualTo(dogs);
    }

    /**
     * Тестирование метода <b>getAll()</b> в DogService
     * <br>
     * Mockito: когда вызывается метод <b>DogRepository::findAll</b>,
     * возвращается пустая коллекция собак <b>adopterDogs</b>
     */
    @Test
    @DisplayName("Проверка поиска всех собак и возвращения из базы данных пустого списка")
    public void getAllDogsTestReturnsEmpty() {
        List<Dog> dogs = new ArrayList<>();
        when(dogRepository.findAll()).thenReturn(dogs);
        assertThat(dogService.getAll()).isEqualTo(dogs);
    }
}
