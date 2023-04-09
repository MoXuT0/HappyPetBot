package com.team4.happydogbot.service;

import com.team4.happydogbot.config.BotConfig;
import com.team4.happydogbot.entity.AdopterCat;
import com.team4.happydogbot.entity.AdopterDog;
import com.team4.happydogbot.entity.ReportCat;
import com.team4.happydogbot.entity.ReportDog;
import com.team4.happydogbot.repository.AdopterCatRepository;
import com.team4.happydogbot.repository.AdopterDogRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.*;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BotTest {
    @Mock
    private BotConfig botConfig;
    @Mock
    private AdopterCatRepository adopterCatRepository;
    @Mock
    private AdopterDogRepository adopterDogRepository;
    @Spy
    @InjectMocks
    private Bot bot;

    private final AdopterDog expectedDog = new AdopterDog();
    private final AdopterCat expectedCat = new AdopterCat();

    @Test
    @DisplayName("Проверка сохранения отчета, если фото отправлено как фото")
    public void getReportDogTestAsPhoto() {
        Update update = new Update();
        update.setMessage(new Message());
        update.getMessage().setCaption("Рацион: Шарик кушает прекрасно, утром чаппи из пакетика, днем сухой корм, вечером лакомство, налитую водичку за день выпивает полностью; Самочувствие: Шарик очень активно бегает, прыгает, просит с ним поиграть, к новому месту адаптировался быстро, занет где его место; Поведение: Шарик изучил дом, знает где свое место, ночью спит там, перестал лаять на членов семьи");
        ArrayList<PhotoSize> photoSizes = new ArrayList<>();
        update.getMessage().setPhoto(photoSizes);
        PhotoSize photoSize = new PhotoSize();
        photoSizes.add(photoSize);
        photoSize.setFileId("TestFileId123");
        update.getMessage().setChat(new Chat());
        update.getMessage().getChat().setId(123L);

        AdopterDog adopterDog = new AdopterDog();
        when(adopterDogRepository.findAdopterDogByChatId(any(Long.class))).thenReturn(adopterDog);
        ReportDog expected = new ReportDog();
        expected.setFileId("TestFileId123");
        expected.setCaption("Рацион: Шарик кушает прекрасно, утром чаппи из пакетика, днем сухой корм, вечером лакомство, налитую водичку за день выпивает полностью; Самочувствие: Шарик очень активно бегает, прыгает, просит с ним поиграть, к новому месту адаптировался быстро, занет где его место; Поведение: Шарик изучил дом, знает где свое место, ночью спит там, перестал лаять на членов семьи");
        adopterDog.setReports(new ArrayList<>());
        adopterDog.getReports().add(expected);

        bot.getReport(update, true);

        ArgumentCaptor<AdopterDog> argumentCaptor = ArgumentCaptor.forClass(AdopterDog.class);
        verify(adopterDogRepository).save(argumentCaptor.capture());
        AdopterDog actualAdopter = argumentCaptor.getValue();
        ReportDog actual = actualAdopter.getReports().get(0);

        Assertions.assertThat(actual.getFileId()).isEqualTo(expected.getFileId());
        Assertions.assertThat(actual.getCaption()).isEqualTo(expected.getCaption());
        Assertions.assertThat(actual.getExamination()).isEqualTo(expected.getExamination());
        Assertions.assertThat(actual.getAdopterDog()).isEqualTo(expected.getAdopterDog());
    }

    @Test
    @DisplayName("Проверка сохранения отчета, если фото отправлено как документ")
    public void getReportCatTestAsDocument() {
        Update update = new Update();
        update.setMessage(new Message());
        update.getMessage().setCaption("Рацион: Шарик кушает прекрасно, утром чаппи из пакетика, днем сухой корм, вечером лакомство, налитую водичку за день выпивает полностью; Самочувствие: Шарик очень активно бегает, прыгает, просит с ним поиграть, к новому месту адаптировался быстро, занет где его место; Поведение: Шарик изучил дом, знает где свое место, ночью спит там, перестал лаять на членов семьи");
        Document document = new Document();
        update.getMessage().setDocument(document);
        document.setFileId("TestFileId123");
        update.getMessage().setChat(new Chat());
        update.getMessage().getChat().setId(123L);

        AdopterCat adopterCat = new AdopterCat();
        when(adopterCatRepository.findAdopterCatByChatId(any(Long.class))).thenReturn(adopterCat);
        ReportCat expected = new ReportCat();
        expected.setFileId("TestFileId123");
        expected.setCaption("Рацион: Шарик кушает прекрасно, утром чаппи из пакетика, днем сухой корм, вечером лакомство, налитую водичку за день выпивает полностью; Самочувствие: Шарик очень активно бегает, прыгает, просит с ним поиграть, к новому месту адаптировался быстро, занет где его место; Поведение: Шарик изучил дом, знает где свое место, ночью спит там, перестал лаять на членов семьи");
        adopterCat.setReports(new ArrayList<>());
        adopterCat.getReports().add(expected);

        bot.getReport(update, false);

        ArgumentCaptor<AdopterCat> argumentCaptor = ArgumentCaptor.forClass(AdopterCat.class);
        verify(adopterCatRepository).save(argumentCaptor.capture());
        AdopterCat actualAdopter = argumentCaptor.getValue();
        ReportCat actual = actualAdopter.getReports().get(0);

        Assertions.assertThat(actual.getFileId()).isEqualTo(expected.getFileId());
        Assertions.assertThat(actual.getCaption()).isEqualTo(expected.getCaption());
        Assertions.assertThat(actual.getExamination()).isEqualTo(expected.getExamination());
        Assertions.assertThat(actual.getAdopterCat()).isEqualTo(expected.getAdopterCat());
    }

    @Test
    @DisplayName("Проверка сохранения отчета, если подпись к фото не прошла валидацию - отчет на сохранен")
    public void getReportTestWithIncorrectCaption() {
        Update update = new Update();
        update.setMessage(new Message());
        update.getMessage().setCaption("Рацион: Гуд; Самочувствие: гуд; Поведение: гуд");
        update.getMessage().setChat(new Chat());
        update.getMessage().getChat().setId(123L);

        bot.getReport(update, true);
    }

    @Test
    @DisplayName("Проверка изменения статуса пользователя на приют для собак, новый пользователь")
    public void changeUserStatusOfShelterToDogNewUserTest() {
        Long chatId = 123L;
        boolean isDog = true;

        when(adopterDogRepository.findAdopterDogByChatId(any(Long.class))).thenReturn(null);
        when(adopterCatRepository.findAdopterCatByChatId(any(Long.class))).thenReturn(null);
        expectedDog.setChatId(chatId);
        expectedDog.setDog(isDog);

        bot.changeUserStatusOfShelter(chatId, isDog);

        ArgumentCaptor<AdopterDog> argumentCaptor = ArgumentCaptor.forClass(AdopterDog.class);
        verify(adopterDogRepository).save(argumentCaptor.capture());
        AdopterDog actualAdopter = argumentCaptor.getValue();

        Assertions.assertThat(actualAdopter.getChatId()).isEqualTo(expectedDog.getChatId());
        Assertions.assertThat(actualAdopter.isDog()).isEqualTo(expectedDog.isDog());
    }

    @Test
    @DisplayName("Проверка изменения статуса пользователя на приют для собак, пользователь уже был в приюте кошек")
    public void changeUserStatusOfShelterToDogWhenWasCatTest() {
        Long chatId = 123L;
        boolean isDog = true;

        AdopterCat adopterCat = new AdopterCat();
        adopterCat.setChatId(chatId);
        adopterCat.setDog(false);
        when(adopterDogRepository.findAdopterDogByChatId(any(Long.class))).thenReturn(null);
        when(adopterCatRepository.findAdopterCatByChatId(any(Long.class))).thenReturn(adopterCat);
        expectedDog.setChatId(chatId);
        expectedCat.setChatId(chatId);
        expectedDog.setDog(isDog);
        expectedCat.setDog(isDog);

        bot.changeUserStatusOfShelter(chatId, isDog);

        ArgumentCaptor<AdopterDog> argumentCaptorDog = ArgumentCaptor.forClass(AdopterDog.class);
        verify(adopterDogRepository).save(argumentCaptorDog.capture());
        AdopterDog actualAdopterDog = argumentCaptorDog.getValue();
        ArgumentCaptor<AdopterCat> argumentCaptorCat = ArgumentCaptor.forClass(AdopterCat.class);
        verify(adopterCatRepository).save(argumentCaptorCat.capture());
        AdopterCat actualAdopterCat = argumentCaptorCat.getValue();

        Assertions.assertThat(actualAdopterDog.getChatId()).isEqualTo(expectedDog.getChatId());
        Assertions.assertThat(actualAdopterDog.isDog()).isEqualTo(expectedDog.isDog());
        Assertions.assertThat(actualAdopterCat.getChatId()).isEqualTo(expectedCat.getChatId());
        Assertions.assertThat(actualAdopterCat.isDog()).isEqualTo(expectedCat.isDog());
    }

    @Test
    @DisplayName("Проверка изменения статуса пользователя на приют для кошек, новый пользователь")
    public void changeUserStatusOfShelterToCatNewUserTest() {
        Long chatId = 123L;
        boolean isDog = false;

        when(adopterDogRepository.findAdopterDogByChatId(any(Long.class))).thenReturn(null);
        when(adopterCatRepository.findAdopterCatByChatId(any(Long.class))).thenReturn(null);
        expectedCat.setChatId(chatId);
        expectedCat.setDog(isDog);

        bot.changeUserStatusOfShelter(chatId, isDog);

        ArgumentCaptor<AdopterCat> argumentCaptor = ArgumentCaptor.forClass(AdopterCat.class);
        verify(adopterCatRepository).save(argumentCaptor.capture());
        AdopterCat actualAdopter = argumentCaptor.getValue();

        Assertions.assertThat(actualAdopter.getChatId()).isEqualTo(expectedCat.getChatId());
        Assertions.assertThat(actualAdopter.isDog()).isEqualTo(expectedCat.isDog());
    }

    @Test
    @DisplayName("Проверка изменения статуса пользователя на приют для кошек, пользователь уже был в приюте собак")
    public void changeUserStatusOfShelterToCatWhenWasDogTest() {
        Long chatId = 123L;
        boolean isDog = false;

        AdopterDog adopterDog = new AdopterDog();
        adopterDog.setChatId(chatId);
        adopterDog.setDog(true);
        when(adopterDogRepository.findAdopterDogByChatId(any(Long.class))).thenReturn(adopterDog);
        when(adopterCatRepository.findAdopterCatByChatId(any(Long.class))).thenReturn(null);
        expectedDog.setChatId(chatId);
        expectedCat.setChatId(chatId);
        expectedDog.setDog(isDog);
        expectedCat.setDog(isDog);

        bot.changeUserStatusOfShelter(chatId, isDog);

        ArgumentCaptor<AdopterDog> argumentCaptorDog = ArgumentCaptor.forClass(AdopterDog.class);
        verify(adopterDogRepository).save(argumentCaptorDog.capture());
        AdopterDog actualAdopterDog = argumentCaptorDog.getValue();
        ArgumentCaptor<AdopterCat> argumentCaptorCat = ArgumentCaptor.forClass(AdopterCat.class);
        verify(adopterCatRepository).save(argumentCaptorCat.capture());
        AdopterCat actualAdopterCat = argumentCaptorCat.getValue();

        Assertions.assertThat(actualAdopterDog.getChatId()).isEqualTo(expectedDog.getChatId());
        Assertions.assertThat(actualAdopterDog.isDog()).isEqualTo(expectedDog.isDog());
        Assertions.assertThat(actualAdopterCat.getChatId()).isEqualTo(expectedCat.getChatId());
        Assertions.assertThat(actualAdopterCat.isDog()).isEqualTo(expectedCat.isDog());
    }

}
