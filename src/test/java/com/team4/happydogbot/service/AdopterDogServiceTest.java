package com.team4.happydogbot.service;

import com.team4.happydogbot.entity.*;
import com.team4.happydogbot.exception.AdopterDogNotFoundException;
import com.team4.happydogbot.repository.AdopterDogRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Тест - класс для проверки CRUD операций в классе - сервисе усыновителя кота
 * @see AdopterDog
 * @see AdopterDogRepository
 * @see AdopterDogService
 * @see AdopterDogServiceTest
 */
@ExtendWith(MockitoExtension.class)
public class AdopterDogServiceTest {

    @Mock
    AdopterDogRepository adopterDogRepository;

    @InjectMocks
    AdopterDogService adopterDogService;

    private final AdopterDog expected = new AdopterDog();
    private final AdopterDog expected1 = new AdopterDog();

    @BeforeEach
    public void setUp() {
        expected.setChatId(1234567890L);
        expected.setFirstName("Ivan");
        expected.setLastName("Ivanov");
        expected.setUserName("iiivanov");
        expected.setAge(33);
        expected.setAddress("МСК...");
        expected.setTelephoneNumber("7951...");
        expected.setState(Status.REGISTRATION);

        expected1.setChatId(9876543210L);
        expected1.setFirstName("Petr");
        expected1.setLastName("Petrov");
        expected1.setUserName("pppetrov");
        expected1.setAge(23);
        expected1.setAddress("МСК...");
        expected1.setTelephoneNumber("7902...");
        expected1.setState(Status.REGISTRATION);
    }

    /**
     * Тестирование метода <b>add()</b> в AdopterDogService
     * <br>
     * Mockito: когда вызывается метод <b>AdopterDogRepository::save</b>,
     * возвращается усыновитель собака <b>expected</b>
     */
    @Test
    @DisplayName("Проверка добавления нового усыновителя собаки и сохранения его в базе данных")
    public void addAdopterDogTest() {
        when(adopterDogRepository.save(any(AdopterDog.class))).thenReturn(expected);

        AdopterDog actual = adopterDogService.add(expected);

        Assertions.assertThat(actual.getFirstName()).isEqualTo(expected.getFirstName());
        Assertions.assertThat(actual.getLastName()).isEqualTo(expected.getLastName());
        Assertions.assertThat(actual.getUserName()).isEqualTo(expected.getUserName());
        Assertions.assertThat(actual.getAge()).isEqualTo(expected.getAge());
        Assertions.assertThat(actual.getAddress()).isEqualTo(expected.getAddress());
        Assertions.assertThat(actual.getTelephoneNumber()).isEqualTo(expected.getTelephoneNumber());
        Assertions.assertThat(actual.getState()).isEqualTo(expected.getState());
    }

    /**
     * Тестирование метода <b>get()</b> в AdopterDogService
     * <br>
     * Mockito: когда вызывается метод <b>AdopterDogRepository::findById</b>,
     * возвращается усыновитель собака <b>expected</b>
     */
    @Test
    @DisplayName("Проверка поиска усыновителя собаки по id и возвращения его из базы данных")
    public void getByIdAdopterDogTest() {
        when(adopterDogRepository.findById(any(Long.class))).thenReturn(Optional.of(expected));

        AdopterDog actual = adopterDogService.get(expected.getChatId());

        Assertions.assertThat(actual.getFirstName()).isEqualTo(expected.getFirstName());
        Assertions.assertThat(actual.getLastName()).isEqualTo(expected.getLastName());
        Assertions.assertThat(actual.getUserName()).isEqualTo(expected.getUserName());
        Assertions.assertThat(actual.getAge()).isEqualTo(expected.getAge());
        Assertions.assertThat(actual.getAddress()).isEqualTo(expected.getAddress());
        Assertions.assertThat(actual.getTelephoneNumber()).isEqualTo(expected.getTelephoneNumber());
        Assertions.assertThat(actual.getState()).isEqualTo(expected.getState());
    }

    /**
     * Тест на создание исключения в методе <b>get()</b> в AdopterDogService
     * <br>
     * Mockito: когда вызывается метод <b>AdopterDogRepository::findById</b>,
     * выбрасывается исключение <b>AdopterDogNotFoundException</b>
     * @throws AdopterDogNotFoundException
     */
    @Test
    @DisplayName("Проверка выброса исключения в методе поиска усыновителя собаки")
    public void getByIdAdopterDogExceptionTest() {
        when(adopterDogRepository.findById(any(Long.class))).thenThrow(AdopterDogNotFoundException.class);
        org.junit.jupiter.api.Assertions.assertThrows(AdopterDogNotFoundException.class,
                () -> adopterDogService.get(0L));
    }

    /**
     * Тестирование метода <b>update()</b> в AdopterDogService
     * <br>
     * Mockito: когда вызывается метод <b>AdopterDogRepository::findById</b> и <b>AdopterDogRepository::save</b>,
     * возвращается отредактированный усыновитель собаки <b>expected</b>
     */
    @Test
    @DisplayName("Проверка редактирования усыновителя собаки, сохранения и возвращения его из базы данных")
    public void updateAdopterDogTest() {
        when(adopterDogRepository.findById(any(Long.class))).thenReturn(Optional.of(expected));
        when(adopterDogRepository.save(any(AdopterDog.class))).thenReturn(expected);

        AdopterDog actual = adopterDogService.update(expected);

        Assertions.assertThat(actual.getFirstName()).isEqualTo(expected.getFirstName());
        Assertions.assertThat(actual.getLastName()).isEqualTo(expected.getLastName());
        Assertions.assertThat(actual.getUserName()).isEqualTo(expected.getUserName());
        Assertions.assertThat(actual.getAge()).isEqualTo(expected.getAge());
        Assertions.assertThat(actual.getAddress()).isEqualTo(expected.getAddress());
        Assertions.assertThat(actual.getTelephoneNumber()).isEqualTo(expected.getTelephoneNumber());
        Assertions.assertThat(actual.getState()).isEqualTo(expected.getState());
    }

    /**
     * Тест на создание исключения в методе <b>update()</b> в AdopterDogService
     * <br>
     * Mockito: когда вызывается метод <b>AdopterDogRepository::findById</b>,
     * выбрасывается исключение <b>AdopterDogNotFoundException</b>
     * @throws AdopterDogNotFoundException
     */
    @Test
    @DisplayName("Проверка выброса исключения в методе редактирования усыновителя собаки")
    public void updateAdopterDogExceptionTest() {
        when(adopterDogRepository.findById(any(Long.class))).thenThrow(AdopterDogNotFoundException.class);
        org.junit.jupiter.api.Assertions.assertThrows(AdopterDogNotFoundException.class,
                () -> adopterDogService.update(expected));
    }

    /**
     * Тестирование метода <b>getAll()</b> в AdopterDogService
     * <br>
     * Mockito: когда вызывается метод <b>AdopterDogRepository::findAll</b>,
     * возвращается коллекция усыновителей собак <b>adopterDogs</b>
     */
    @Test
    @DisplayName("Проверка поиска всех усыновителей собак и возвращения их из базы данных")
    public void getAllAdopterDogsTest() {
        List<AdopterDog> adopterDogs = new ArrayList<>();
        adopterDogs.add(expected);
        adopterDogs.add(expected1);

        when(adopterDogRepository.findAll()).thenReturn(adopterDogs);

        Collection<AdopterDog> actual = adopterDogService.getAll();

        Assertions.assertThat(actual.size()).isEqualTo(adopterDogs.size());
        Assertions.assertThat(actual).isEqualTo(adopterDogs);
    }

    /**
     * Тестирование метода <b>getAll()</b> в AdopterDogService
     * <br>
     * Mockito: когда вызывается метод <b>AdopterDogRepository::findAll</b>,
     * возвращается пустая коллекция усыновителей собак <b>adopterDogs</b>
     */
    @Test
    @DisplayName("Проверка поиска всех усыновителей собак и возвращения из базы данных пустого списка")
    public void getAllAdopterDogsTestReturnsEmpty() {
        List<AdopterDog> adopterDogs = new ArrayList<>();
        when(adopterDogRepository.findAll()).thenReturn(adopterDogs);
        assertThat(adopterDogService.getAll()).isEqualTo(adopterDogs);
    }

    /**
     * Тестирование метода <b>update()</b> в AdopterDogService
     * <br>
     * Mockito: когда вызывается метод <b>AdopterDogRepository::findById</b> и <b>AdopterDogRepository::save</b>,
     * возвращается отредактированный усыновитель с собакой <b>expected</b>
     */
    @Test
    @DisplayName("Проверка добавления собаки в качестве поля объекта усыновитель")
    public void updateFieldDogIdByAdopterCat() {
        expected.setDog(new Dog(2L, "Sharik", "Buldog", 2018, "Test2"));
        when(adopterDogRepository.findById(any(Long.class))).thenReturn(Optional.of(expected));
        when(adopterDogRepository.save(any(AdopterDog.class))).thenReturn(expected);

        AdopterDog actual = adopterDogService.update(expected);

        Assertions.assertThat(actual.getDog()).isEqualTo(expected.getDog());
    }

    /**
     * Тестирование метода <b>update()</b> в AdopterDogService
     * <br>
     * Mockito: когда вызывается метод <b>AdopterDogRepository::findById</b> и <b>AdopterDogRepository::save</b>,
     * возвращается отредактированный усыновитель с коллекцией отчетов о собаках <b>reportDogs</b>
     */
    @Test
    @DisplayName("Проверка добавления списка отчетов в качестве поля объекта усыновитель")
    public void updateFieldReportAdopterDog() {
        ReportDog expectedTest1 = new ReportDog();
        expectedTest1.setId(1L);
        expectedTest1.setReportDate(LocalDate.of(2023, 3, 24));
        expectedTest1.setFileId("Test124578");
        expectedTest1.setCaption("Рацион: гуд; Самочувствие: гуд; Поведение: гуд");
        expectedTest1.setExamination(null);

        ReportDog expectedTest2 = new ReportDog();
        expectedTest2.setId(2L);
        expectedTest2.setReportDate(LocalDate.of(2023, 3, 24));
        expectedTest2.setFileId("Test986532");
        expectedTest2.setCaption("Рацион: гуд; Самочувствие: гуд; Поведение: гуд");
        expectedTest2.setExamination(true);

        List<ReportDog> reportDogs = new ArrayList<>();
        reportDogs.add(expectedTest1);
        reportDogs.add(expectedTest2);

        expected.setReports(reportDogs);

        when(adopterDogRepository.findById(any(Long.class))).thenReturn(Optional.of(expected));
        when(adopterDogRepository.save(any(AdopterDog.class))).thenReturn(expected);

        AdopterDog actual = adopterDogService.update(expected);

        Assertions.assertThat(actual.getChatId()).isEqualTo(expected.getChatId());
        Assertions.assertThat(actual.getReports()).isEqualTo(expected.getReports());
    }
}
