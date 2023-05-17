package com.example.telegrambotanimalshelter;
import com.example.telegrambotanimalshelter.listener.TelegramBotUpdatesListener;
import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Objects;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class TelegramBotUpdatesListenerTest {
    @Mock
    private TelegramBot telegramBot;
    @InjectMocks
    private TelegramBotUpdatesListener telegramBotUpdatesListener;

    @Test
    public void testProcess() throws URISyntaxException, IOException {
        // Prepare test data
        String json = Files.readString(Path.of(Objects.requireNonNull(TelegramBotUpdatesListenerTest.class.getResource("update.json")).toURI()));
        Update update = BotUtils.fromJson(json.replace("%text%", "/start"), Update.class);
        SendResponse sendResponse = BotUtils.fromJson(""" 
        {
        "ok": true
        }
        """, SendResponse.class);

        when(telegramBot.execute(any())).thenReturn(sendResponse);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> sendMessageCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(sendMessageCaptor.capture());
        SendMessage sentMessage = sendMessageCaptor.getValue();

        org.assertj.core.api.Assertions.assertThat(sentMessage.getParameters().get("chat_id")).isEqualTo(update.message().chat().id());
        org.assertj.core.api.Assertions.assertThat(sentMessage.getParameters().get("text")).isEqualTo("Привет! Я бот-помощник для поиска приюта для животных. " +
                "Я могу помочь тебе выбрать приют для кошек или для собак. Выбери, пожалуйста, один из приютов:");
    }
}

