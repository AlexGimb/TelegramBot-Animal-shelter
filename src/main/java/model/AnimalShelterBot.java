package model;

import configuration.TelegramBotConfiguration;
import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnimalShelterBot extends TelegramLongPollingBot {

    // мапа пользователей
    private Map<Long, UserState> users = new HashMap<>();
    // лист кнопок
    List<KeyboardRow> keyboard = new ArrayList<>();
    TelegramBotConfiguration config;

    private static final String CATS = "Приют для кошек";
    private static final String DOGS = "Приют для собак";
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
            Message message = update.getMessage();
            // получаем текст сообщения от пользователя
            String text = message.getText();
            // чат пользователя
            Long chatId = message.getChatId();
            User user = message.getFrom();
            // имя пользователя
            String firstName = user.getFirstName();
            // проверяем, является ли пользователь новым или уже общался с ботом ранее
            if (!users.containsKey(chatId)) {
                // если пользователь новый, приветствуем его и запрашиваем выбор приюта
                sendMessage(chatId, "Привет, " + firstName + "! Я бот, который поможет тебе найти питомца. Я могу помочь тебе найти приют для кошек или для собак. Какой приют тебя интересует?");
                // создаем кнопки
                KeyboardButton button1 = new KeyboardButton("Приют для кошек");
                KeyboardButton button2 = new KeyboardButton("Приют для собак");
                // создаем строку клавиатуры и добавляем в нее кнопки
                KeyboardRow shelter = new KeyboardRow();
                shelter.add(button1);
                shelter.add(button2);
                // создаем список строк клавиатуры и добавляем в него строку с кнопками
                List<KeyboardRow> keyboard = new ArrayList<>();
                keyboard.add(shelter);
                // создаем объект клавиатуры
                ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(keyboard);
                // указывает, что клавиатура должна изменять размер, чтобы соответствовать количеству кнопок, которые мы добавляем в неё.
                keyboardMarkup.setResizeKeyboard(true);
                // указывает, что клавиатура должна исчезнуть после того, как пользователь выберет одну из кнопок.
                keyboardMarkup.setOneTimeKeyboard(true);
                // указывает, что клавиатура будет показана только тем пользователям, которые взаимодействуют с ботом, а не всем пользователям, которые видят сообщение от бота в групповом чате.
                keyboardMarkup.setSelective(true);
                // отправляем клавиатуру пользователю
                sendMessage(chatId, "Выберите приют:", keyboardMarkup);
                // добавляем нового пользователя в мапу
                UserState userState = new UserState();
                users.put(chatId, userState);
                userState.setStage(UserState.SELECT_ACTION);
            } else {
                // пользователь уже начал общение, определяем текущий этап
                UserState userState = users.get(chatId);
                switch (userState.getStage()) {
                    case UserState.SELECT_SHELTER:
                        // пользователь выбрал приют, предлагаем дальнейшие действия
                        if (text.equals("Приют для кошек")) {
                            userState.setShelter(CATS);
                        } else if (text.equals("Приют для собак")) {
                            userState.setShelter(DOGS);
                        } else {
                            // пользователь выбрал неверный вариант, предлагаем позвать волонтера
                            sendMessage(chatId, "Я не понимаю, что вы хотите. Хотите, чтобы я позвал волонтера?");
                            userState.setStage(Stage.CALL_VOLUNTEER.ordinal());
                            break;
                        }
                    case UserState.SELECT_ACTION:
                        // предлагаем дальнейшие действия
                        KeyboardButton button1 = new KeyboardButton("Узнать информацию о приюте");
                        KeyboardButton button2 = new KeyboardButton("Как взять животное из приюта");
                        KeyboardButton button3 = new KeyboardButton("Прислать отчет о питомце");
                        KeyboardButton button4 = new KeyboardButton("Позвать волонтера");
                        KeyboardRow row1 = new KeyboardRow();
                        row1.add(button1);
                        row1.add(button2);
                        KeyboardRow row2 = new KeyboardRow();
                        row2.add(button3);
                        row2.add(button4);
                        List<KeyboardRow> keyboard1 = new ArrayList<>();
                        keyboard1.add(row1);
                        keyboard1.add(row2);
                        ReplyKeyboardMarkup keyboardMenu = new ReplyKeyboardMarkup(keyboard1);
                        keyboardMenu.setResizeKeyboard(true);
                        keyboardMenu.setOneTimeKeyboard(true);
                        keyboardMenu.setSelective(true);
                        sendMessage(chatId, "Выберите действие:", keyboardMenu);
                        if (text.equals("Узнать информацию о приюте")) {
                        } else if (text.equals("Как взять животное из приюта")) {
                            sendMessage(chatId, "Для того чтобы взять животное, вам необходимо связаться с работником приюта");
                        } else if (text.equals("Прислать отчет о питомце")) {
                            sendMessage(chatId, "Мы получили ваш запрос на отчет. Ожидайте ответа работника приюта.");
                        } else if (text.equals("Позвать волонтера")) {
                            sendMessage(chatId, "Передал информацию, в ближайшее время с вами свяжется волонтер");
                        }
                        break;
                }

            }
        }
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        try {
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(Long chatId, String text, ReplyKeyboardMarkup keyboardMarkup) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setReplyMarkup(keyboardMarkup);
        try {
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}