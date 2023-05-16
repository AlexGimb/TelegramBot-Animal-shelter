package com.example.telegrambotanimalshelter.listener;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Класс, реализующий функцию обращения к Telegram API и получения обновлений
 */
@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    /**
     * Поле класса для логирования
     */
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    /**
     * Внедрение зависимостей Телеграмм бот
     */
    private final TelegramBot telegramBot;

    /**
     * Конструктор TelegramBotUpdatesListener
     * @param telegramBot
     */
    public TelegramBotUpdatesListener(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    /**
     * Метод инициализации обновлений телеграмм бота
     */
    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    /**
     * Метод, осуществляющий получение массива обновлений и реализующий логику согласно, полученных данных
     * @param updates
     * @return
     */
    @Override
    public int process(List<Update> updates) {
        try {
            updates.stream()
                    .filter(update -> update.message() != null)
                    .forEach(update -> {
                        logger.info("Processing update: {}", update);
                        Message message = update.message();
                        Long chatId = message.chat().id();
                        String text = message.text();

                        switch (text) {
                            case "/start":
                                sendStartMessage(chatId);



                            default:
                                sendDefaultMessage(chatId);
                                break;
                        }
                    });
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void sendStartMessage(Long chatId) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(
                new InlineKeyboardButton[] {
                        new InlineKeyboardButton("Кошки").callbackData("cats"),
                        new InlineKeyboardButton("Собаки").callbackData("dogs")
                }
        );

        SendMessage sendMessage = new SendMessage(chatId, "Привет! Я бот-помощник для поиска приюта для животных. " +
                "Я могу помочь тебе выбрать приют для кошек или для собак. " +
                "Выбери, пожалуйста, один из вариантов:");
        sendMessage.replyMarkup(keyboardMarkup);
        sendTelegramMessage(sendMessage);
    }

    private void sendDefaultMessage(Long chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "Вы указали неверную команду. " +
                "Пожалуйста, начните с команды /start");
        sendTelegramMessage(sendMessage);
    }

    private void sendTelegramMessage(SendMessage sendMessage) {
        SendResponse sendResponse = telegramBot.execute(sendMessage);
        if (!sendResponse.isOk()) {
            logger.error("Error during sending message: {}", sendResponse.description());
        }
    }

    /**
     * Внутренний метод для отправки сообщений боту и получения ответа
     * @param chatId
     * @param message
     */
    @Nullable
    private void sendMessage(Long chatId, String message) {
        SendMessage sendMessage = new SendMessage(chatId, message);
        SendResponse sendResponse = telegramBot.execute(sendMessage);
        if (!sendResponse.isOk()) {
            logger.error("Error during sending message: {}", sendResponse.description());
        }
    }
}
