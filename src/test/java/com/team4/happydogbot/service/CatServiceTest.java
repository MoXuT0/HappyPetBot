package com.team4.happydogbot.service;

import com.team4.happydogbot.entity.Cat;
import com.team4.happydogbot.exception.CatNotFoundException;
import com.team4.happydogbot.repository.CatRepository;
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
 * Тест - класс для проверки CRUD операций в классе - сервисе кота
 * @see Cat
 * @see CatRepository
 * @see CatService
 * @see CatServiceTest
 */
@ExtendWith(MockitoExtension.class)
public class CatServiceTest {

    @Mock
    CatRepository catRepository;

    @InjectMocks
    CatService catService;

    private final Cat expected = new Cat();
    private final Cat expected1 = new Cat();

    @BeforeEach
    public void setUp() {
        expected.setId(1L);
        expected.setName("Ponchik");
        expected.setBreed("Bolinez");
        expected.setYearOfBirth(2020);
        expected.setDescription("Test");

        expected1.setId(2L);
        expected1.setName("Bublik");
        expected1.setBreed("Siam");
        expected1.setYearOfBirth(2017);
        expected1.setDescription("Test");
    }

    /**
     * Тестирование метода <b>add()</b> в CatService
     * <br>
     * Mockito: когда вызывается метод <b>CatRepository::save</b>, возвращается кот <b>expected</b>
     */
    @Test
    @DisplayName("Проверка добавления новой кота и сохранения его в базе данных")
    public void addCatTest() {
        when(catRepository.save(any(Cat.class))).thenReturn(expected);

        Cat actual = catService.add(expected);

        Assertions.assertThat(actual.getName()).isEqualTo(expected.getName());
        Assertions.assertThat(actual.getBreed()).isEqualTo(expected.getBreed());
        Assertions.assertThat(actual.getYearOfBirth()).isEqualTo(expected.getYearOfBirth());
        Assertions.assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
    }

    /**
     * Тестирование метода <b>get()</b> в CatService
     * <br>
     * Mockito: когда вызывается метод <b>CatRepository::findById</b>,
     * возвращается кот <b>expected</b>
     */
    @Test
    @DisplayName("Проверка поиска кота по id и возвращения его из базы данных")
    public void getByIdCatTest() {
        when(catRepository.findById(any(Long.class))).thenReturn(Optional.of(expected));

        Cat actual = catService.get(expected.getId());

        Assertions.assertThat(actual.getName()).isEqualTo(expected.getName());
        Assertions.assertThat(actual.getBreed()).isEqualTo(expected.getBreed());
        Assertions.assertThat(actual.getYearOfBirth()).isEqualTo(expected.getYearOfBirth());
        Assertions.assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
    }

    /**
     * Тест на создание исключения в методе <b>get()</b> в CatService
     * <br>
     * Mockito: когда вызывается метод <b>CatRepository::findById</b>,
     * выбрасывается исключение <b>CatNotFoundException</b>
     * @throws CatNotFoundException
     */
    @Test
    @DisplayName("Проверка выброса исключения в методе поиска кота")
    public void getByIdCatExceptionTest() {
        when(catRepository.findById(any(Long.class))).thenThrow(CatNotFoundException.class);
        org.junit.jupiter.api.Assertions.assertThrows(CatNotFoundException.class, () -> catService.get(0L));
    }

    /**
     * Тестирование метода <b>update()</b> в CatService
     * <br>
     * Mockito: когда вызывается метод <b>CatRepository::findById</b> и <b>CatRepository::save</b>,
     * возвращается отредактированный кот <b>expected</b>
     */
    @Test
    @DisplayName("Проверка редактирования кота, сохранения и возвращения его из базы данных")
    public void updateCatTest() {
        when(catRepository.findById(any(Long.class))).thenReturn(Optional.of(expected));
        when(catRepository.save(any(Cat.class))).thenReturn(expected);

        Cat actual = catService.update(expected);

        Assertions.assertThat(actual.getName()).isEqualTo(expected.getName());
        Assertions.assertThat(actual.getBreed()).isEqualTo(expected.getBreed());
        Assertions.assertThat(actual.getYearOfBirth()).isEqualTo(expected.getYearOfBirth());
        Assertions.assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
    }

    /**
     * Тест на создание исключения в методе <b>update()</b> в CatService
     * <br>
     * Mockito: когда вызывается метод <b>CatRepository::findById</b>,
     * выбрасывается исключение <b>CatNotFoundException</b>
     * @throws CatNotFoundException
     */
    @Test
    @DisplayName("Проверка выброса исключения в методе редактирования кота")
    public void updateCatExceptionTest() {
        when(catRepository.findById(any(Long.class))).thenThrow(CatNotFoundException.class);
        org.junit.jupiter.api.Assertions.assertThrows(CatNotFoundException.class,
                () -> catService.update(expected));
    }

    /**
     * Тестирование метода <b>getAll()</b> в CatService
     * <br>
     * Mockito: когда вызывается метод <b>CatRepository::findAll</b>,
     * возвращается коллекция котов <b>cats</b>
     */
    @Test
    @DisplayName("Проверка поиска всех котов и возвращения их из базы данных")
    public void getAllCatsTest() {
        List<Cat> cats = new ArrayList<>();
        cats.add(expected);
        cats.add(expected1);

        when(catRepository.findAll()).thenReturn(cats);

        Collection<Cat> actual = catService.getAll();

        Assertions.assertThat(actual.size()).isEqualTo(cats.size());
        Assertions.assertThat(actual).isEqualTo(cats);
    }

    /**
     * Тестирование метода <b>getAll()</b> в CatService
     * <br>
     * Mockito: когда вызывается метод <b>CatRepository::findAll</b>,
     * возвращается пустая коллекция усыновителей котов <b>cats</b>
     */
    @Test
    @DisplayName("Проверка поиска всех котов и возвращения из базы данных пустого списка")
    public void getAllCatsTestReturnsEmpty() {
        List<Cat> cats = new ArrayList<>();
        when(catRepository.findAll()).thenReturn(cats);
        assertThat(catService.getAll()).isEqualTo(cats);
    }
}
