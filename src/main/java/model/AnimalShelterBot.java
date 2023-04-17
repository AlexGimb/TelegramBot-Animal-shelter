package model;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class AnimalShelterBot extends TelegramLongPollingBot {

    public AnimalShelterBot(String username, String token) {

    }

    @Override
    public String getBotToken() {
        // Возвращает токен нашего бота
        return "5829700568:AAFSyuvtIF_zSXYGJK-Jf7J9GiVrGRI4M2A";
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }

    @Override
    public String getBotUsername() {
        // Возвращает имя нашего бота
        return "AnimalShelterBot";
    }


    @Override
    public void onUpdateReceived(Update update) {
        // Обработка входящего сообщения от пользователя
        if (update.hasMessage() && update.getMessage().hasText()) {
            // Получаем текст сообщения
            String messageText = update.getMessage().getText();
            // Получаем ID чата
            Long chatId = update.getMessage().getChatId();
            // Создаем объект для отправки сообщения
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId.toString());
            sendMessage.setText("Здравствуйте,выберите пункт меню");
            try {
                // Отправляем сообщение
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}
