package com.team4.happydogbot.service;

import com.team4.happydogbot.entity.AdopterCat;
import com.team4.happydogbot.entity.Cat;
import com.team4.happydogbot.entity.ReportCat;
import com.team4.happydogbot.entity.Status;
import com.team4.happydogbot.exception.AdopterCatNotFoundException;
import com.team4.happydogbot.repository.AdopterCatRepository;
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
 * @see AdopterCat
 * @see AdopterCatRepository
 * @see AdopterCatService
 * @see AdopterCatServiceTest
 */
@ExtendWith(MockitoExtension.class)
public class AdopterCatServiceTest {

    @Mock
    AdopterCatRepository adopterCatRepository;

    @InjectMocks
    AdopterCatService adopterCatService;

    private final AdopterCat expected = new AdopterCat();
    private final AdopterCat expected1 = new AdopterCat();

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
     * Тестирование метода <b>add()</b> в AdopterCatService
     * <br>
     * Mockito: когда вызывается метод <b>AdopterCatRepository::save</b>,
     * возвращается усыновитель кота <b>expected</b>
     */
    @Test
    @DisplayName("Проверка добавления нового усыновителя кота и сохранения его в базе данных")
    public void addAdopterCatTest() {
        when(adopterCatRepository.save(any(AdopterCat.class))).thenReturn(expected);

        AdopterCat actual = adopterCatService.add(expected);

        Assertions.assertThat(actual.getFirstName()).isEqualTo(expected.getFirstName());
        Assertions.assertThat(actual.getLastName()).isEqualTo(expected.getLastName());
        Assertions.assertThat(actual.getUserName()).isEqualTo(expected.getUserName());
        Assertions.assertThat(actual.getAge()).isEqualTo(expected.getAge());
        Assertions.assertThat(actual.getAddress()).isEqualTo(expected.getAddress());
        Assertions.assertThat(actual.getTelephoneNumber()).isEqualTo(expected.getTelephoneNumber());
        Assertions.assertThat(actual.getState()).isEqualTo(expected.getState());
    }

    /**
     * Тестирование метода <b>get()</b> в AdopterCatService
     * <br>
     * Mockito: когда вызывается метод <b>AdopterCatRepository::findById</b>,
     * возвращается усыновитель кота <b>expected</b>
     */
    @Test
    @DisplayName("Проверка поиска усыновителя кота по id и возвращения его из базы данных")
    public void getByIdAdopterCatTest() {
        when(adopterCatRepository.findById(any(Long.class))).thenReturn(Optional.of(expected));

        AdopterCat actual = adopterCatService.get(expected.getChatId());

        Assertions.assertThat(actual.getFirstName()).isEqualTo(expected.getFirstName());
        Assertions.assertThat(actual.getLastName()).isEqualTo(expected.getLastName());
        Assertions.assertThat(actual.getUserName()).isEqualTo(expected.getUserName());
        Assertions.assertThat(actual.getAge()).isEqualTo(expected.getAge());
        Assertions.assertThat(actual.getAddress()).isEqualTo(expected.getAddress());
        Assertions.assertThat(actual.getTelephoneNumber()).isEqualTo(expected.getTelephoneNumber());
        Assertions.assertThat(actual.getState()).isEqualTo(expected.getState());
    }

    /**
     * Тест на создание исключения в методе <b>get()</b> в AdopterCatService
     * <br>
     * Mockito: когда вызывается метод <b>AdopterCatRepository::findById</b>,
     * выбрасывается исключение <b>AdopterCatNotFoundException</b>
     * @throws AdopterCatNotFoundException
     */
    @Test
    @DisplayName("Проверка выброса исключения в методе поиска усыновителя кота")
    public void getByIdAdopterCatExceptionTest() {
        when(adopterCatRepository.findById(any(Long.class))).thenThrow(AdopterCatNotFoundException.class);
        org.junit.jupiter.api.Assertions.assertThrows(AdopterCatNotFoundException.class,
                () -> adopterCatService.get(0L));
    }

    /**
     * Тестирование метода <b>update()</b> в AdopterCatService
     * <br>
     * Mockito: когда вызывается метод <b>AdopterCatRepository::findById</b> и <b>AdopterCatRepository::save</b>,
     * возвращается отредактированный усыновитель кота <b>expected</b>
     */
    @Test
    @DisplayName("Проверка редактирования усыновителя кота, сохранения и возвращения его из базы данных")
    public void updateAdopterCatTest() {
        when(adopterCatRepository.findById(any(Long.class))).thenReturn(Optional.of(expected));
        when(adopterCatRepository.save(any(AdopterCat.class))).thenReturn(expected);

        AdopterCat actual = adopterCatService.update(expected);

        Assertions.assertThat(actual.getFirstName()).isEqualTo(expected.getFirstName());
        Assertions.assertThat(actual.getLastName()).isEqualTo(expected.getLastName());
        Assertions.assertThat(actual.getUserName()).isEqualTo(expected.getUserName());
        Assertions.assertThat(actual.getAge()).isEqualTo(expected.getAge());
        Assertions.assertThat(actual.getAddress()).isEqualTo(expected.getAddress());
        Assertions.assertThat(actual.getTelephoneNumber()).isEqualTo(expected.getTelephoneNumber());
        Assertions.assertThat(actual.getState()).isEqualTo(expected.getState());
    }

    /**
     * Тест на создание исключения в методе <b>update()</b> в AdopterCatService
     * <br>
     * Mockito: когда вызывается метод <b>AdopterCatRepository::findById</b>,
     * выбрасывается исключение <b>AdopterCatNotFoundException</b>
     * @throws AdopterCatNotFoundException
     */
    @Test
    @DisplayName("Проверка выброса исключения в методе редактирования усыновителя кота")
    public void updateAdopterCatExceptionTest() {
        when(adopterCatRepository.findById(any(Long.class))).thenThrow(AdopterCatNotFoundException.class);
        org.junit.jupiter.api.Assertions.assertThrows(AdopterCatNotFoundException.class,
                () -> adopterCatService.update(expected));
    }

    /**
     * Тестирование метода <b>getAll()</b> в AdopterCatService
     * <br>
     * Mockito: когда вызывается метод <b>AdopterCatRepository::findAll</b>,
     * возвращается коллекция усыновителей котов <b>adopterCats</b>
     */
    @Test
    @DisplayName("Проверка поиска всех усыновителей котов и возвращения их из базы данных")
    public void getAllAdopterCatsTest() {
        List<AdopterCat> adopterCats = new ArrayList<>();
        adopterCats.add(expected);
        adopterCats.add(expected1);

        when(adopterCatRepository.findAll()).thenReturn(adopterCats);

        Collection<AdopterCat> actual = adopterCatService.getAll();

        Assertions.assertThat(actual.size()).isEqualTo(adopterCats.size());
        Assertions.assertThat(actual).isEqualTo(adopterCats);
    }

    /**
     * Тестирование метода <b>getAll()</b> в AdopterCatService
     * <br>
     * Mockito: когда вызывается метод <b>AdopterCatRepository::findAll</b>,
     * возвращается пустая коллекция усыновителей котов <b>adopterCats</b>
     */
    @Test
    @DisplayName("Проверка поиска всех усыновителей котов и возвращения из базы данных пустого списка")
    public void getAllAdopterCatsTestReturnsEmpty() {
        List<AdopterCat> adopterCats = new ArrayList<>();
        when(adopterCatRepository.findAll()).thenReturn(adopterCats);
        assertThat(adopterCatService.getAll()).isEqualTo(adopterCats);
    }

    /**
     * Тестирование метода <b>update()</b> в AdopterCatService
     * <br>
     * Mockito: когда вызывается метод <b>AdopterCatRepository::findById</b> и <b>AdopterCatRepository::save</b>,
     * возвращается отредактированный усыновитель с котом <b>expected</b>
     */
    @Test
    @DisplayName("Проверка добавления кота в качестве поля объекта усыновитель")
    public void updateFieldCatIdByAdopterCat() {
        expected.setCat(new Cat(1L, "Ponchik", "Bolinez", 2020, "Test1"));
        when(adopterCatRepository.findById(any(Long.class))).thenReturn(Optional.of(expected));
        when(adopterCatRepository.save(any(AdopterCat.class))).thenReturn(expected);

        AdopterCat actual = adopterCatService.update(expected);

        Assertions.assertThat(actual.getChatId()).isEqualTo(expected.getChatId());
        Assertions.assertThat(actual.getCat()).isEqualTo(expected.getCat());
    }

    /**
     * Тестирование метода <b>update()</b> в AdopterCatService
     * <br>
     * Mockito: когда вызывается метод <b>AdopterCatRepository::findById</b> и <b>AdopterCatRepository::save</b>,
     * возвращается отредактированный усыновитель с коллекцией отчетов о котах <b>adopterCats</b>
     */
    @Test
    @DisplayName("Проверка добавления списка отчетов в качестве поля объекта усыновитель")
    public void updateFieldReportAdopterCat() {
        ReportCat expectedTest1 = new ReportCat();
        expectedTest1.setId(1L);
        expectedTest1.setReportDate(LocalDate.of(2023, 3, 24));
        expectedTest1.setFileId("Test124578");
        expectedTest1.setCaption("Рацион: гуд; Самочувствие: гуд; Поведение: гуд");
        expectedTest1.setExamination(null);

        ReportCat expectedTest2 = new ReportCat();
        expectedTest2.setId(2L);
        expectedTest2.setReportDate(LocalDate.of(2023, 3, 24));
        expectedTest2.setFileId("Test986532");
        expectedTest2.setCaption("Рацион: гуд; Самочувствие: гуд; Поведение: гуд");
        expectedTest2.setExamination(true);

        List<ReportCat> reportCats = new ArrayList<>();
        reportCats.add(expectedTest1);
        reportCats.add(expectedTest2);

        expected.setReports(reportCats);

        when(adopterCatRepository.findById(any(Long.class))).thenReturn(Optional.of(expected));
        when(adopterCatRepository.save(any(AdopterCat.class))).thenReturn(expected);

        AdopterCat actual = adopterCatService.update(expected);

        Assertions.assertThat(actual.getChatId()).isEqualTo(expected.getChatId());
        Assertions.assertThat(actual.getReports()).isEqualTo(expected.getReports());
    }
}
