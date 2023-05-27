package com.example.telegrambotanimalshelter.listener;

import com.example.telegrambotanimalshelter.entity.AppUser;
import com.example.telegrambotanimalshelter.entity.UserState;
import com.example.telegrambotanimalshelter.repository.UserRepository;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.example.telegrambotanimalshelter.entity.UserState.WAITING_CONTACTS;

/**
 * Класс, реализующий функцию обращения к Telegram API и получения обновлений
 */
@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    private final Pattern numberPattern = Pattern.compile("^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{10}$");
    /**
     * Поле класса для логирования
     */
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    /**
     * Внедрение зависимостей Телеграмм бот
     */
    private final TelegramBot telegramBot;
    private final UserRepository userRepository;

    private final Map<Long, UserState> userStates = new HashMap<>();
    /**
     * Конструктор TelegramBotUpdatesListener
     *
     */
    public TelegramBotUpdatesListener(TelegramBot telegramBot, UserRepository userRepository) {
        this.telegramBot = telegramBot;
        this.userRepository = userRepository;
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
     *
     */
    @Override
    public int process(List<Update> updates) {
        try {
            updates.forEach(update -> {
                if (update.callbackQuery() != null) {
                    logger.info("Processing update: {}", update);
                    CallbackQuery callbackQuery = update.callbackQuery();
                    String data = callbackQuery.data();
                    Message message = callbackQuery.message();
                    Long chatId = message.chat().id();

                    // Обработка нажатия кнопок
                    if ("cats".equals(data)) {
                        sendCatsMenu(chatId);
                    } else if ("dogs".equals(data)) {
                        sendDogsMenu(chatId);
                    } else if ("info_cat".equals(data)) {
                        sendInfoShelterCat(chatId);
                    } else if ("info_dog".equals(data)) {
                        sendInfoShelterDog(chatId);
                    } else if ("take".equals(data)) {
                        sendTakeMessage(chatId);
                    } else if ("send".equals(data)) {
                        sendSendMessage(chatId);
                    } else if ("help".equals(data)) {
                        sendHelpMessage(chatId);
                    } else if ("contacts".equals(data)) {
                        contactData(chatId);
                    }
                    // Отправка подтверждения о выполнении команды
                    AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery(callbackQuery.id());
                    telegramBot.execute(answerCallbackQuery);

                } else if (update.message() != null) {
                    // Обработка текстовых сообщений
                    Message message = update.message();
                    String text = message.text();
                    Long chatId = message.chat().id();

                    UserState currentState = userStates.getOrDefault(chatId, UserState.DEFAULT);

                    if (text.equals("/start")) {
                        sendStartMessage(chatId);
                        createChoiceOfShelterMenu();
                    } else if (text.equals("Выбрать приют")) {
                        sendStartMenu(chatId);
                    } else if (currentState.equals(WAITING_CONTACTS)) {
                        saveUserInfo(chatId, text);
                    } else {
                        sendDefaultMessage(chatId);
                    }
                }
            });
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private ReplyKeyboardMarkup createChoiceOfShelterMenu() {
        KeyboardButton choiceOfShelter = new KeyboardButton("Выбрать приют");
        return new ReplyKeyboardMarkup(choiceOfShelter)
                .resizeKeyboard(true)
                .oneTimeKeyboard(true);
    }


    private void sendStartMessage(Long chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "Привет! Я бот-помощник для поиска приюта для животных. " +
                "Я могу помочь тебе выбрать приют для кошек или для собак");
        sendMessage.replyMarkup(createChoiceOfShelterMenu());
        sendTelegramMessage(sendMessage);
    }

    private void sendStartMenu(Long chatId) {
        InlineKeyboardMarkup inlinekeyboardMarkup = new InlineKeyboardMarkup(
                new InlineKeyboardButton("Кошки").callbackData("cats"),
                new InlineKeyboardButton("Собаки").callbackData("dogs"));
        inlinekeyboardMarkup.addRow(new InlineKeyboardButton("Оставить контакты").callbackData("contacts"));
        SendMessage sendMessage = new SendMessage(chatId, "Выбери, пожалуйста, один из приютов:");
        sendMessage.replyMarkup(inlinekeyboardMarkup);
        sendTelegramMessage(sendMessage);
        userStates.put(chatId, UserState.DEFAULT);
    }

    private void sendCatsMenu(Long chatId) {
        InlineKeyboardMarkup inlinekeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton infoCat = new InlineKeyboardButton("Узнать информацию о приюте").callbackData("info_cat");
        InlineKeyboardButton take = new InlineKeyboardButton("Как взять животное из приюта").callbackData("take");
        InlineKeyboardButton send = new InlineKeyboardButton("Прислать отчет о питомце").callbackData("send");
        InlineKeyboardButton help = new InlineKeyboardButton("Позвать волонтера").callbackData("help");
        inlinekeyboardMarkup.addRow(infoCat);
        inlinekeyboardMarkup.addRow(take);
        inlinekeyboardMarkup.addRow(send);
        inlinekeyboardMarkup.addRow(help);
        SendMessage sendMessage = new SendMessage(chatId, "Выбери, пожалуйста, один из вариантов:");
        sendMessage.replyMarkup(inlinekeyboardMarkup);
        sendTelegramMessage(sendMessage);
    }

    private void sendDogsMenu(Long chatId) {
        InlineKeyboardMarkup inlinekeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton infoDog = new InlineKeyboardButton("Узнать информацию о приюте").callbackData("info_dog");
        InlineKeyboardButton take = new InlineKeyboardButton("Как взять животное из приюта").callbackData("take");
        InlineKeyboardButton send = new InlineKeyboardButton("Прислать отчет о питомце").callbackData("send");
        InlineKeyboardButton help = new InlineKeyboardButton("Позвать волонтера").callbackData("help");
        inlinekeyboardMarkup.addRow(infoDog);
        inlinekeyboardMarkup.addRow(take);
        inlinekeyboardMarkup.addRow(send);
        inlinekeyboardMarkup.addRow(help);
        SendMessage sendMessage = new SendMessage(chatId, "Выбери, пожалуйста, один из вариантов:");
        sendMessage.replyMarkup(inlinekeyboardMarkup);
        sendTelegramMessage(sendMessage);
    }


    private void sendInfoShelterCat(Long chatId) {
        // Логика обработки кнопки /info_cat
        SendMessage sendMessage = new SendMessage(chatId, "Приют для кошек Мурёнка расположен на  территории, " +
                "на которой размещены отдельные утепленные домики с летним выгулом для кошечек и хозяйственные пристройки, " +
                "различного назначения. Имеется ветеринарный блок с карантинным помещением и изолятором. Все животные проходят вакцинацию " +
                "против инфекционных заболеваний, опасных для человека, обработку против экто и эндопаразитов, стерилизации и кастрацию, " +
                "также ежедневно осуществляется клинический осмотр животных. Кормят кошек  профессиональными  кормами, " +
                "как сухими, так и жидкими в соответствии с рекомендациями ветеринарных врачей и физиологическими особенностями. " +
                "За каждым животным закреплён специальный сотрудник по уходу, который персонально отвечает за них, расчёсывает, играет.");
        sendTelegramMessage(sendMessage);
    }

    private void sendInfoShelterDog(Long chatId) {
        // Логика обработки кнопки /info_dog
        SendMessage sendMessage = new SendMessage(chatId, "Приют расположен на большой территории, на которой размещены утеплённые вольеры," +
                " выгульные площадки и хозяйственные пристройки. Имеется ветеринарный блок с карантинным помещением и изолятором. " +
                "Все животные проходят вакцинацию против инфекционных заболеваний, опасных для человека, обработку против экто и эндопаразитов," +
                " стерилизацию и кастрацию, также ежедневно осуществляется клинический осмотр животных. Кормят собак профессиональными " +
                "сухими кормами в соответствии с рекомендациями ветеринарных врачей и физиологическими особенностями. " +
                "За каждым животным закреплён специальный сотрудник по уходу, который персонально отвечает за него, " +
                "выгуливает, расчёсывает, играет, учит собак элементарным командам и ходить на поводке – адаптирует собак к " +
                "будущей домашней жизни");
        sendTelegramMessage(sendMessage);
    }

    private void sendTakeMessage(Long chatId) {
        // Логика обработки кнопки /take
        SendMessage sendMessage = new SendMessage(chatId, """
                Итак, если вы хотите завести питомца и уверены, что хотите забрать его из приюта, то вот несколько простых шагов:
                1. Удостоверьтесь, что у вас и у членов вашей семьи нет аллергии на животных.
                2. Для того, что бы не тратить время на поиск животного в приюте, вы можете перед визитом ознакомится с некоторыми из них на нашем сайте или на волонтерских страничках в социальных сетях - Волонтерские странички приюта Дворняга - vkontakte
                3. После этого можно приехать в приют и познакомиться с понравившимся животным. C собачкой лучшей выйти на прогулку на специально отведенной территории и познакомится там ближе с характером животного.
                4. После того, как вы поймете, что именно эту  собачку вы хотите забрать из приюта, необходимо пообщаться с работником приюта или с курирующим волонтером, чтобы узнать про особенности поведения, содержания и питания животного, а также рассказать в каких условиях в дальнейшем будет жить питомец.
                5. Следующий этап - это оформление договора в нем фиксируются данные обеих сторон, оговариваются пункты ответственного содержания животного.
                6. На следующий день после подписания договора, вы можете приехать и забрать питомца домой. Для собачки следует привезти поводок с ошейником.""");
        sendTelegramMessage(sendMessage);
    }

    private void sendSendMessage(Long chatId, Message message) {
        // Логика обработки кнопки /send
        initiateReportDialog(chatId);
        waitingForReport.put(chatId, true);
    if (waitingForReport.get(chatId) == null) {
        telegramBot.execute(new SendMessage(chatId, "Нажмите кнопку отправить отчет")
                .replyMarkup(new InlineKeyboardMarkup(new InlineKeyboardButton("Отправить отчет")
                        .callbackData("SendReport"))));
    } else if (message.text() != null && (message.photo() == null && message.document() == null) && waitingForReport.get(chatId)) {
        telegramBot.execute(new SendMessage(chatId, "Вы не приложили фото к отчету"));
    } else if (message.caption() == null && (message.photo() != null || message.document().mimeType().equals("image/jpeg")) && waitingForReport.get(chatId)) {
        telegramBot.execute(new SendMessage(chatId, "Вы не приложили отчет"));
    } else if (message.caption() != null && (message.photo() != null || message.document().mimeType().equals("image/jpeg")) && waitingForReport.get(chatId)) {
        byte[] photoAsByteArray = reportCatService.processAttachment(message);
        if (photoAsByteArray == null) {
            throw new RuntimeException("no photo from tg downloaded");
        }
        reportCatService.addReport(chatId, message.caption(), photoAsByteArray);
        telegramBot.execute(new SendMessage(chatId, "добавляем отчёт"));
        waitingForReport.remove(chatId);
    } else {
        sendDefaultMessage(chatId);
    }
    }

    private void sendHelpMessage(Long chatId) {
        // Логика обработки кнопки /help
        SendMessage sendMessage = new SendMessage(chatId, "В ближайшее время с вами свяжется волонтер или " +
                "можете связаться с волонтерами по телефону +79871235647");
        sendTelegramMessage(sendMessage);
    }


    private void contactData(Long chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "Напишите ваше имя и номер телефона");
        sendTelegramMessage(sendMessage);
    }

    private void contactData(Long chatId) {
        SendMessage sendMessage = new SendMessage(chatId, """
                Напишите ваше имя и номер телефона
                Вначале укажите имя и через пробел номер телефона (+79... или 89..)
                Пример:
                Иван +7 900 00 000 00""");
        sendTelegramMessage(sendMessage);
        userStates.put(chatId, WAITING_CONTACTS);
    }

    private void saveUserInfo(Long chatId, String text) {
        // Логика сохранения в БД
        // Обработка контактных данных
        String[] parts = text.split(" ", 2);
        if (!text.isEmpty() && parts.length == 2) {
            String userName = parts[0];
            String userPhone = parts[1];
            if (checkNumberForCorrect(userPhone)) {
                AppUser appUser = new AppUser(chatId, userName, userPhone);
                userRepository.save(appUser);
                saveUserInfoMessage(chatId);
                userStates.remove(chatId);
            } else {
                sendWrongNumber(chatId);
            }
        } else {
            sendWrongNumber(chatId);
        }
    }

    private boolean checkNumberForCorrect(String number) {
        // Проверка номера на корректность
        return numberPattern.matcher(number).matches();
    }


    private void sendWrongNumber(Long chatId) {
        // Сообщение при неверном указание контактных данных
        SendMessage sendMessage = new SendMessage(chatId, """
                Вы указали некорректный номер телефона или имя
                Вначале укажите имя и через пробел номер телефона (+79... или 89..)
                Пример:
                Иван +79000000000
                """);
        sendTelegramMessage(sendMessage);
    }

    private void saveUserInfoMessage(Long chatId) {
        // Логика обработки кнопки InfoMessage
        SendMessage sendMessage = new SendMessage(chatId, "Контактные данные сохранены");
        sendTelegramMessage(sendMessage);
        sendStartMenu(chatId);
    }

    private void sendDefaultMessage(Long chatId) {
        // Сообщение по дефолту
        SendMessage sendMessage = new SendMessage(chatId, "Вы указали неверную команду. " +
                "Пожалуйста, начните с выбора приюта");
        sendTelegramMessage(sendMessage);
        sendStartMenu(chatId);
    }

    private void sendTelegramMessage(SendMessage sendMessage) {
        SendResponse sendResponse = telegramBot.execute(sendMessage);
        if (!sendResponse.isOk()) {
            logger.error("Error during sending message: {}", sendResponse.description());
        }
    }

    private void saveUserInfo(Long chatId, String text) {
        // Логика сохранения в БД
        String[] parts = text.split(" ");
        if (parts.length == 2) {
            String userName = parts[0];
            String userPhone = parts[1];
            if (checkNumberForCorrect(userPhone)) {
                AppUser appUser = new AppUser(chatId, userName, userPhone);
                userService.saveUser(appUser);
                saveUserInfoMessage(chatId);
            } else {
                sendWrongNumber(chatId);
            }
        } else {
            sendWrongNumber(chatId);
        }
    }

    private boolean checkNumberForCorrect(String number) {
        // Проверка номера на корректность
        return numberPattern.matcher(number).matches();
    }


    private void sendWrongNumber(Long chatId) {
        // Сообщение при неверном указание контактных данных
        SendMessage sendMessage = new SendMessage(chatId, """
                Вы указали некорректный номер телефона или имя
                Вначале укажите имя и через пробел номер телефона (+79... или 89..)
                Пример:
                Иван +7 900 000 00 00
                """);
        sendTelegramMessage(sendMessage);
    }

    private void saveUserInfoMessage(Long chatId) {
        // Логика обработки кнопки InfoMessage
        SendMessage sendMessage = new SendMessage(chatId, "Контактные данные сохранены");
        sendTelegramMessage(sendMessage);
    }

    private void initiateReportDialog(long chatId) {
        SendMessage sendMessage = new SendMessage(chatId, """
                Пожалуйста, заполните отчёт по следующим пунктам:
                1) Рацион животного.
                2) Общее самочувствие и привыкание к новому месту.
                3) Изменение в поведении: отказ от старых привычек, приобретение новых.
                Также, не забудьте прикрепить к сообщению фото животного.
                """);
        telegramBot.execute(sendMessage);
    }
}
