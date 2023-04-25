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
            // создаем кнопки
            KeyboardButton catButton = new KeyboardButton("Приют для кошек");
            KeyboardButton dogButton = new KeyboardButton("Приют для собак");
            KeyboardButton infoButton = new KeyboardButton("Узнать информацию о приюте");
            KeyboardButton takeAnAnimalButton = new KeyboardButton("Как взять животное из приюта");
            KeyboardButton reportButton = new KeyboardButton("Прислать отчет о питомце");
            KeyboardButton volunteerButton = new KeyboardButton("Позвать волонтера");
            KeyboardButton BackToMenuShelterButton = new KeyboardButton("Вернуться в меню выбора приюта");
            // создаем строки клавиатуры и добавляем в нее кнопки
            KeyboardRow shelter = new KeyboardRow();
            shelter.add(catButton);
            shelter.add(dogButton);
            KeyboardRow row1 = new KeyboardRow();
            row1.add(infoButton);
            row1.add(takeAnAnimalButton);
            KeyboardRow row2 = new KeyboardRow();
            row2.add(reportButton);
            row2.add(volunteerButton);
            KeyboardRow row3 = new KeyboardRow();
            row3.add(BackToMenuShelterButton);
            // создаем список строк клавиатуры и добавляем в него строку с кнопками
            List<KeyboardRow> keyboard = new ArrayList<>();
            List<KeyboardRow> keyboard1 = new ArrayList<>();
            keyboard.add(shelter);
            keyboard1.add(row1);
            keyboard1.add(row2);
            keyboard1.add(row3);
            // создаем объект клавиатуры
            ReplyKeyboardMarkup keyboardMenuShelter = new ReplyKeyboardMarkup(keyboard);
            ReplyKeyboardMarkup keyboardMenu = new ReplyKeyboardMarkup(keyboard1);
            // указывает, что клавиатура должна изменять размер, чтобы соответствовать количеству кнопок, которые мы добавляем в неё.
            keyboardMenuShelter.setResizeKeyboard(true);
            keyboardMenu.setResizeKeyboard(true);
            // указывает, что клавиатура должна исчезнуть после того, как пользователь выберет одну из кнопок.
            keyboardMenuShelter.setOneTimeKeyboard(true);
            keyboardMenu.setOneTimeKeyboard(true);
            // указывает, что клавиатура будет показана только тем пользователям, которые взаимодействуют с ботом, а не всем пользователям, которые видят сообщение от бота в групповом чате.
            keyboardMenuShelter.setSelective(true);
            keyboardMenu.setSelective(true);
            // проверяем, является ли пользователь новым или уже общался с ботом ранее
            if (!users.containsKey(chatId)) {
                // если пользователь новый, приветствуем его и запрашиваем выбор приюта
                sendMessage(chatId, "Привет, " + firstName + "! Я бот, который поможет тебе найти питомца. Я могу помочь тебе найти приют для кошек или для собак");
                // добавляем нового пользователя в мапу
                UserState userState = new UserState();
                users.put(chatId, userState);
                sendMessage(chatId, "Выберите приют:", keyboardMenuShelter);
                userState.setStage(UserState.SELECT_SHELTER);
            } else {
                // пользователь уже начал общение, определяем текущий этап
                UserState userState = users.get(chatId);
                switch (userState.getStage()) {
                    case UserState.SELECT_SHELTER:
                        // отправляем клавиатуру пользователю
                        if (text.equals("Вернуться в меню выбора приюта")) {
                            sendMessage(chatId, "Выберите приют:", keyboardMenuShelter);
                            break;
                        }else if (text.equals("Приют для кошек")) {
                            userState.setStage(UserState.SELECT_ACTION_CAT);
                            break;
                        } else if (text.equals("Приют для собак")) {
                            userState.setStage(UserState.SELECT_ACTION_DOG);
                            break;
                        } else {
                            // пользователь выбрал неверный вариант, предлагаем позвать волонтера
                            sendMessage(chatId, "Я не понимаю, что вы хотите. В ближайшее время с вами свяжется волонтер");
                            userState.setStage(UserState.SELECT_SHELTER);
                            break;
                        }
                    // пользователь выбрал приют, предлагаем дальнейшие действия
                    case UserState.SELECT_ACTION_CAT:
                        // предлагаем дальнейшие действия
                        sendMessage(chatId, "Выберите действие:", keyboardMenu);
                        if (text.equals("Узнать информацию о приюте")) {
                            sendMessage(chatId, "Приют для кошек Мурёнка расположен на  территории, " +
                                    "на которой размещены отдельные утепленные домики с летним выгулом для кошечек и хозяйственные пристройки," +
                                    " различного назначения.\n" +
                                    "Имеется ветеринарный блок с карантинным помещением и изолятором. " +
                                    "Все животные проходят вакцинацию против инфекционных заболеваний, " +
                                    "опасных для человека, обработку против экто и эндопаразитов, стерилизации и кастрацию, " +
                                    "также ежедневно осуществляется клинический осмотр животных.\n" +
                                    "Кормят кошек  профессиональными  кормами, как сухими, так и жидкими в соответствии с " +
                                    "рекомендациями ветеринарных врачей и физиологическими особенностями. " +
                                    "За каждым животным закреплён специальный сотрудник по уходу, который персонально отвечает за них, " +
                                    "расчёсывает, играет.");
                            break;
                        } else if (text.equals("Как взять животное из приюта")) {
                            sendMessage(chatId, "Для того чтобы взять животное, вам необходимо связаться с работником приюта");
                            break;
                        } else if (text.equals("Прислать отчет о питомце")) {
                            sendMessage(chatId, "Мы получили ваш запрос на отчет. Ожидайте ответа работника приюта.");
                            break;
                        } else if (text.equals("Позвать волонтера")) {
                            sendMessage(chatId, "Передал информацию, в ближайшее время с вами свяжется волонтер");
                            break;
                        } else if (text.equals("Вернуться в меню выбора приюта")) {
                            userState.setStage(UserState.SELECT_SHELTER);
                            break;
                        }
                        break;
                    case UserState.SELECT_ACTION_DOG:
                        // предлагаем дальнейшие действия
                        sendMessage(chatId, "Выберите действие:", keyboardMenu);
                        if (text.equals("Узнать информацию о приюте")) {
                            sendMessage(chatId, "Приют расположен на большой территории, на которой размещены утеплённые вольеры, " +
                                    "выгульные площадки и хозяйственные пристройки.\n" +
                                    "Имеется ветеринарный блок с карантинным помещением и изолятором. " +
                                    "Все животные проходят вакцинацию против инфекционных заболеваний, " +
                                    "опасных для человека, обработку против экто и эндопаразитов, стерилизацию и кастрацию, " +
                                    "также ежедневно осуществляется клинический осмотр животных.\n" +
                                    "Кормят собак профессиональными сухими кормами в соответствии с рекомендациями ветеринарных врачей " +
                                    "и физиологическими особенностями. За каждым животным закреплён специальный сотрудник по уходу, " +
                                    "который персонально отвечает за него, выгуливает, расчёсывает, играет, " +
                                    "учит собак элементарным командам и ходить на поводке – адаптирует собак к будущей домашней жизни.");
                            break;
                        } else if (text.equals("Как взять животное из приюта")) {
                            sendMessage(chatId, "Для того чтобы взять животное, вам необходимо связаться с работником приюта");
                            break;
                        } else if (text.equals("Прислать отчет о питомце")) {
                            sendMessage(chatId, "Мы получили ваш запрос на отчет. Ожидайте ответа работника приюта.");
                            break;
                        } else if (text.equals("Позвать волонтера")) {
                            sendMessage(chatId, "Передал информацию, в ближайшее время с вами свяжется волонтер");
                            break;
                        } else if (text.equals("Вернуться в меню выбора приюта")) {
                            userState.setStage(UserState.SELECT_SHELTER);
                            break;
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