package model;

import configuration.TelegramBotConfiguration;
import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class AnimalShelterBot extends TelegramLongPollingBot {
    private static final int STATE_START = 0;
    private static final int STATE_CHOOSE_SHELTER = 1;
    private static final int STATE_CHOOSE_ACTION = 2;
    private static final int STATE_REPORT = 3;
    private static final int STATE_VOLUNTEER = 4;

    private int state = STATE_START;
    TelegramBotConfiguration config;

    @SneakyThrows
    @Override
    // Возвращает имя нашего бота
    public String getBotUsername() {
        config = new TelegramBotConfiguration();
        return config.getTelegramBotName();
    }

    @SneakyThrows
    @Override
    // Возвращает токен нашего бота
    public String getBotToken() {
        config = new TelegramBotConfiguration();
        return config.getTelegramBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            // обработка сообщения в зависимости от текущего состояния пользователя
            switch (state) {
                case STATE_START:
                    // приветствие пользователя и запрос выбора приюта
                    sendMessage(chatId, "Здравствуйте! Я бот приюта для животных. " +
                            "Пожалуйста, выберите приют для кошек или собак.");
                    state = STATE_CHOOSE_SHELTER;
                    break;
                case STATE_CHOOSE_SHELTER:
                    if (message.equalsIgnoreCase("приют для кошек")
                            || message.equalsIgnoreCase("кошек")
                            || message.equalsIgnoreCase("для кошек")) {
                        // пользователь выбрал приют для кошек
                        sendMessage(chatId, "Вы выбрали приют для кошек.");
                        state = STATE_CHOOSE_ACTION;
                    } else if (message.equalsIgnoreCase("приют для собак")
                            || message.equalsIgnoreCase("собак")
                            || message.equalsIgnoreCase("для собак")) {
                        // пользователь выбрал приют для собак
                        sendMessage(chatId, "Вы выбрали приют для собак." +
                                "\n 1. Узнать информацию о приюте " +
                                "\n 2. Как взять животное из приюта");
                        state = STATE_CHOOSE_ACTION;
                    } else {
                        // пользователь ввел неверный ответ
                        sendMessage(chatId, "Пожалуйста, выберите приют для кошек или собак.");
                    }
                    break;
                case STATE_CHOOSE_ACTION:
                    // пользователь выбирает действие
                    if (message.equalsIgnoreCase("узнать информацию о приюте") || message.equalsIgnoreCase("1")) {
                        // пользователь выбрал узнать информацию о приюте
                        sendMessage(chatId, "Что вы хотели бы узнать о приюте?");
                        state = STATE_REPORT;
                    } else if (message.equalsIgnoreCase("как взять животное из приюта") || message.equalsIgnoreCase("2")) {
                        // пользователь выбрал как взять животное из приюта
                        sendMessage(chatId, "Чтобы забрать животное из приюта, вам нужно сначала обратиться к менеджеру приюта. " +
                                "Он расскажет вам, какие условия необходимо выполнить и какие документы предоставить.");
                        state = STATE_CHOOSE_ACTION;
                    } else if (message.equalsIgnoreCase("прислать отчет о питомце")) {
                        // пользователь выбрал прислать отчет о питомце
                        sendMessage(chatId, "Для того, чтобы получить отчет о питомце, напишите свой запрос менеджеру приюта.");
                        state = STATE_CHOOSE_ACTION;
                    } else if (message.equalsIgnoreCase("позвать волонтера")) {
                        // пользователь выбрал позвать волонтера
                        sendMessage(chatId, "Чтобы позвать волонтера, обратитесь к менеджеру приюта. " +
                                "Он расскажет вам, какие задачи могут быть выполнены волонтерами и как подать заявку.");
                        state = STATE_CHOOSE_ACTION;
                    } else {
                        // пользователь ввел неверный ответ
                        sendMessage(chatId, "Пожалуйста, выберите одно из предложенных действий.");
                    }
                    break;
                case STATE_REPORT:
                    // пользователь отправляет запрос на отчет
                    sendMessage(chatId, "Мы получили ваш запрос на отчет. Ожидайте ответа менеджера приюта.");
                    state = STATE_CHOOSE_ACTION;
                    break;
            }
        }
    }

    /**
     * Метод для отправки сообщения пользователю.
     * @param chatId идентификатор чата с пользователем
     * @param text текст сообщения
     */
    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}