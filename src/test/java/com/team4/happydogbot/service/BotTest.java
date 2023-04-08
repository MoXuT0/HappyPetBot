package com.team4.happydogbot.service;

import com.team4.happydogbot.config.BotConfig;
import com.team4.happydogbot.replies.Reply;
import com.team4.happydogbot.repository.AdopterCatRepository;
import com.team4.happydogbot.repository.AdopterDogRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.internal.verification.Times;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.List;
import static com.team4.happydogbot.constants.BotCommands.*;
import static com.team4.happydogbot.constants.BotReplies.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;


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

    private Reply reply = new Reply(bot);

    @Test
    @DisplayName("Проверяем, что после нажатия на /start бот приветствует пользователя и предлагает выбрать приют")
    public void StartTest() throws TelegramApiException {
        Update update = new Update();
        update.setMessage(new Message());
        update.getMessage().setChat(new Chat());
        update.getMessage().setText(START_CMD);
        update.getMessage().getChat().setId(111111L);
        update.getMessage().getChat().setFirstName("User");

        bot.onUpdateReceived(update);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(bot, new Times(2)).execute(argumentCaptor.capture());

        List<SendMessage> actual = argumentCaptor.getAllValues();
        assertThat(actual.get(0).getChatId()).isEqualTo(update.getMessage().getChatId().toString());
        assertThat(actual.get(0).getText()).isEqualTo("User"+MESSAGE_TEXT_GREETINGS);
        assertThat(actual.get(1).getText()).isEqualTo(MESSAGE_TEXT_CHOOSE_SHELTER);
    }




}