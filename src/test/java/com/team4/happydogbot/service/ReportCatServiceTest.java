package com.team4.happydogbot.service;

import com.team4.happydogbot.entity.ExaminationStatus;
import com.team4.happydogbot.entity.ReportCat;
import com.team4.happydogbot.exception.ReportCatNotFoundException;
import com.team4.happydogbot.repository.ReportCatRepository;
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
 * Тест - класс для проверки CRUD операций в классе - сервисе отчета о коте
 * @see ReportCat
 * @see ReportCatRepository
 * @see ReportCatService
 * @see ReportCatServiceTest
 */
@ExtendWith(MockitoExtension.class)
public class ReportCatServiceTest {

    @Mock
    ReportCatRepository reportCatRepository;

    @InjectMocks
    ReportCatService reportCatService;

    private final ReportCat expected = new ReportCat();
    private final ReportCat expected1 = new ReportCat();

    @BeforeEach
    public void setUp() {
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
     * Тестирование метода <b>add()</b> в ReportCatService
     * <br>
     * Mockito: когда вызывается метод <b>ReportCatRepository::save</b>,
     * возвращается отчет о коте <b>expected</b>
     */
    @Test
    @DisplayName("Проверка добавления нового отчета о коте и сохранения его в базе данных")
    public void addReportCatTest() {
        when(reportCatRepository.save(any(ReportCat.class))).thenReturn(expected);

        ReportCat actual = reportCatService.add(expected);

        Assertions.assertThat(actual.getReportDate()).isEqualTo(expected.getReportDate());
        Assertions.assertThat(actual.getFileId()).isEqualTo(expected.getFileId());
        Assertions.assertThat(actual.getCaption()).isEqualTo(expected.getCaption());
        Assertions.assertThat(actual.getExamination()).isEqualTo(expected.getExamination());
    }

    /**
     * Тестирование метода <b>get()</b> в ReportCatService
     * <br>
     * Mockito: когда вызывается метод <b>ReportCatRepository::findById</b>,
     * возвращается отчет о коте <b>expected</b>
     */
    @Test
    @DisplayName("Проверка поиска отчета о коте по id и возвращения его из базы данных")
    public void getByIdReportCatTest() {
        when(reportCatRepository.findById(any(Long.class))).thenReturn(Optional.of(expected));

        ReportCat actual = reportCatService.get(expected.getId());

        Assertions.assertThat(actual.getReportDate()).isEqualTo(expected.getReportDate());
        Assertions.assertThat(actual.getFileId()).isEqualTo(expected.getFileId());
        Assertions.assertThat(actual.getCaption()).isEqualTo(expected.getCaption());
        Assertions.assertThat(actual.getExamination()).isEqualTo(expected.getExamination());
    }

    /**
     * Тест на создание исключения в методе <b>get()</b> в ReportCatService
     * <br>
     * Mockito: когда вызывается метод <b>ReportCatRepository::findById</b>,
     * выбрасывается исключение <b>ReportCatNotFoundException</b>
     * @throws ReportCatNotFoundException
     */
    @Test
    @DisplayName("Проверка выброса исключения в методе поиска отчета о коте")
    public void getByIdReportCatExceptionTest() {
        when(reportCatRepository.findById(any(Long.class))).thenThrow(ReportCatNotFoundException.class);
        org.junit.jupiter.api.Assertions.assertThrows(ReportCatNotFoundException.class,
                () -> reportCatService.get(0L));
    }

    /**
     * Тестирование метода <b>update()</b> в ReportCatService
     * <br>
     * Mockito: когда вызывается метод <b>ReportCatRepository::findById</b> и <b>ReportCatRepository::save</b>,
     * возвращается отредактированный отчет о коте <b>expected1</b>
     */
    @Test
    @DisplayName("Проверка редактирования отчета о коте, сохранения и возвращения его из базы данных")
    public void updateReportCatTest() {
        when(reportCatRepository.findById(any(Long.class))).thenReturn(Optional.of(expected1));
        when(reportCatRepository.save(any(ReportCat.class))).thenReturn(expected1);

        ReportCat actual = reportCatService.update(expected1);

        Assertions.assertThat(actual.getReportDate()).isEqualTo(expected1.getReportDate());
        Assertions.assertThat(actual.getFileId()).isEqualTo(expected1.getFileId());
        Assertions.assertThat(actual.getCaption()).isEqualTo(expected1.getCaption());
        Assertions.assertThat(actual.getExamination()).isEqualTo(expected1.getExamination());
    }

    /**
     * Тест на создание исключения в методе <b>update()</b> в ReportCatService
     * <br>
     * Mockito: когда вызывается метод <b>ReportCatRepository::findById</b>,
     * выбрасывается исключение <b>ReportCatNotFoundException</b>
     * @throws ReportCatNotFoundException
     */
    @Test
    @DisplayName("Проверка выброса исключения в методе редактирования отчета о коте")
    public void updateReportCatExceptionTest() {
        when(reportCatRepository.findById(any(Long.class))).thenThrow(ReportCatNotFoundException.class);
        org.junit.jupiter.api.Assertions.assertThrows(ReportCatNotFoundException.class,
                () -> reportCatService.update(expected1));
    }

    /**
     * Тестирование метода <b>getAll()</b> в ReportCatService
     * <br>
     * Mockito: когда вызывается метод <b>ReportCatRepository::findAll</b>,
     * возвращается коллекция отчетов о котах <b>reportCats</b>
     */
    @Test
    @DisplayName("Проверка поиска всех отчетов о котах и возвращения их из базы данных")
    public void getAllReportCatsTest() {
        List<ReportCat> reportCats = new ArrayList<>();
        reportCats.add(expected);
        reportCats.add(expected1);

        when(reportCatRepository.findAll()).thenReturn(reportCats);

        Collection<ReportCat> actual = reportCatService.getAll();

        Assertions.assertThat(actual.size()).isEqualTo(reportCats.size());
        Assertions.assertThat(actual).isEqualTo(reportCats);
    }

    /**
     * Тестирование метода <b>getAll()</b> в ReportCatService
     * <br>
     * Mockito: когда вызывается метод <b>ReportCatRepository::findAll</b>,
     * возвращается пустая коллекция отчетов о котах <b>reportCats</b>
     */
    @Test
    @DisplayName("Проверка поиска всех отчетов о котах и возвращения из базы данных пустого списка")
    public void getAllReportCatsTestReturnsEmpty() {
        List<ReportCat> reportCats = new ArrayList<>();
        when(reportCatRepository.findAll()).thenReturn(reportCats);
        assertThat(reportCatService.getAll()).isEqualTo(reportCats);
    }
}
