package com.team4.happydogbot.service;

import com.team4.happydogbot.config.BotConfig;
import com.team4.happydogbot.entity.ExaminationStatus;
import com.team4.happydogbot.entity.ReportDog;
import com.team4.happydogbot.exception.ReportDogNotFoundException;
import com.team4.happydogbot.repository.ReportDogRepository;
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
 * Тест - класс для проверки CRUD операций в классе - сервисе отчета о собаке
 * @see ReportDog
 * @see ReportDogRepository
 * @see ReportDogService
 * @see ReportDogServiceTest
 */
@ExtendWith(MockitoExtension.class)
public class ReportDogServiceTest {
    @Mock
    private BotConfig botConfig;
    @Mock
    private ReportDogRepository reportDogRepository;
    @InjectMocks
    private ReportDogService reportDogService;
    @Mock
    private Bot bot;

    private final ReportDog expected = new ReportDog();
    private final ReportDog expected1 = new ReportDog();

    @BeforeEach
    public void setUp() {
//        MockitoAnnotations.initMocks(this);

        expected.setId(1L);
        expected.setReportDate(LocalDate.of(2023, 3, 24));
        expected.setFileId("Test124578");
        expected.setCaption("Рацион: гуд; Самочувствие: гуд; Поведение: гуд");
        expected.setExamination(ExaminationStatus.UNCHECKED);

        expected1.setId(2L);
        expected1.setReportDate(LocalDate.of(2023, 3, 24));
        expected1.setFileId("Test986532");
        expected1.setCaption("Рацион: гуд; Самочувствие: гуд; Поведение: гуд");
        expected1.setExamination(ExaminationStatus.ACCEPTED);
    }

    /**
     * Тестирование метода <b>add()</b> в ReportDogService
     * <br>
     * Mockito: когда вызывается метод <b>ReportDogRepository::save</b>,
     * возвращается отчет о собаке <b>expected</b>
     */
    @Test
    @DisplayName("Проверка добавления нового отчета о собаке и сохранения его в базе данных")
    public void addReportDogTest() {
        when(reportDogRepository.save(any(ReportDog.class))).thenReturn(expected);

        ReportDog actual = reportDogService.add(expected);

        Assertions.assertThat(actual.getReportDate()).isEqualTo(expected.getReportDate());
        Assertions.assertThat(actual.getFileId()).isEqualTo(expected.getFileId());
        Assertions.assertThat(actual.getCaption()).isEqualTo(expected.getCaption());
        Assertions.assertThat(actual.getExamination()).isEqualTo(expected.getExamination());
    }

    /**
     * Тестирование метода <b>get()</b> в ReportDogService
     * <br>
     * Mockito: когда вызывается метод <b>ReportDogRepository::findById</b>,
     * возвращается отчет о собаке <b>expected</b>
     */
    @Test
    @DisplayName("Проверка поиска отчета о собаке по id и возвращения его из базы данных")
    public void getByIdReportDogTest() {
        when(reportDogRepository.findById(any(Long.class))).thenReturn(Optional.of(expected));

        ReportDog actual = reportDogService.get(expected.getId());

        Assertions.assertThat(actual.getReportDate()).isEqualTo(expected.getReportDate());
        Assertions.assertThat(actual.getFileId()).isEqualTo(expected.getFileId());
        Assertions.assertThat(actual.getCaption()).isEqualTo(expected.getCaption());
        Assertions.assertThat(actual.getExamination()).isEqualTo(expected.getExamination());
    }

    /**
     * Тест на создание исключения в методе <b>get()</b> в ReportDogService
     * <br>
     * Mockito: когда вызывается метод <b>ReportDogRepository::findById</b>,
     * выбрасывается исключение <b>ReportDogNotFoundException</b>
     * @throws ReportDogNotFoundException
     */
    @Test
    @DisplayName("Проверка выброса исключения в методе поиска отчета о собаке")
    public void getByIdReportDogExceptionTest() {
        when(reportDogRepository.findById(any(Long.class))).thenThrow(ReportDogNotFoundException.class);
        org.junit.jupiter.api.Assertions.assertThrows(ReportDogNotFoundException.class,
                () -> reportDogService.get(0L));
    }

    /**
     * Тестирование метода <b>update()</b> в ReportDogService
     * <br>
     * Mockito: когда вызывается метод <b>ReportDogRepository::findById</b> и <b>ReportDogRepository::save</b>,
     * возвращается отредактированный отчет о собаке <b>expected1</b>
     */
    @Test
    @DisplayName("Проверка редактирования отчета о собаке, сохранения и возвращения его из базы данных")
    public void updateReportDogTest() {
        when(reportDogRepository.findById(any(Long.class))).thenReturn(Optional.of(expected1));
        when(reportDogRepository.save(any(ReportDog.class))).thenReturn(expected1);

        ReportDog actual = reportDogService.update(expected1);

        Assertions.assertThat(actual.getReportDate()).isEqualTo(expected1.getReportDate());
        Assertions.assertThat(actual.getFileId()).isEqualTo(expected1.getFileId());
        Assertions.assertThat(actual.getCaption()).isEqualTo(expected1.getCaption());
        Assertions.assertThat(actual.getExamination()).isEqualTo(expected1.getExamination());
    }

    /**
     * Тест на создание исключения в методе <b>update()</b> в ReportDogService
     * <br>
     * Mockito: когда вызывается метод <b>ReportDogRepository::findById</b>,
     * выбрасывается исключение <b>ReportDogNotFoundException</b>
     * @throws ReportDogNotFoundException
     */
    @Test
    @DisplayName("Проверка выброса исключения в методе редактирования отчета о собаке")
    public void updateReportDogExceptionTest() {
        when(reportDogRepository.findById(any(Long.class))).thenThrow(ReportDogNotFoundException.class);
        org.junit.jupiter.api.Assertions.assertThrows(ReportDogNotFoundException.class,
                () -> reportDogService.update(expected1));
    }

    /**
     * Тестирование метода <b>getAll()</b> в ReportDogService
     * <br>
     * Mockito: когда вызывается метод <b>ReportDogRepository::findAll</b>,
     * возвращается коллекция отчетов о собаках <b>reportDogs</b>
     */
    @Test
    @DisplayName("Проверка поиска всех отчетов о собаках и возвращения их из базы данных")
    public void getAllReportDogsTest() {
        List<ReportDog> reportDogs = new ArrayList<>();
        reportDogs.add(expected);
        reportDogs.add(expected1);

        when(reportDogRepository.findAll()).thenReturn(reportDogs);

        Collection<ReportDog> actual = reportDogService.getAll();

        Assertions.assertThat(actual.size()).isEqualTo(reportDogs.size());
        Assertions.assertThat(actual).isEqualTo(reportDogs);
    }

    /**
     * Тестирование метода <b>getAll()</b> в ReportDogService
     * <br>
     * Mockito: когда вызывается метод <b>ReportDogRepository::findAll</b>,
     * возвращается пустая коллекция собак <b>adopterDogs</b>
     */
    @Test
    @DisplayName("Проверка поиска всех отчетов о собаках и возвращения из базы данных пустого списка")
    public void getAllReportCatsTestReturnsEmpty() {
        List<ReportDog> reportDogs = new ArrayList<>();
        when(reportDogRepository.findAll()).thenReturn(reportDogs);
        assertThat(reportDogService.getAll()).isEqualTo(reportDogs);
    }
}
