package com.example.telegrambotanimalshelter.listener;
import com.example.telegrambotanimalshelter.entity.AppUser;
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
    private final UserRepository userRepository;

    /**
     * Конструктор TelegramBotUpdatesListener
     * @param telegramBot
     */
    public TelegramBotUpdatesListener(TelegramBot telegramBot,UserRepository userRepository) {
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
     * @param updates
     * @return
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
                            switch (data) {
                                case "cats" -> sendCatsMenu(chatId);
                                case "dogs" -> sendDogsMenu(chatId);
                                case "info_cat" -> sendInfoShelterCat(chatId);
                                case "info_dog" -> sendInfoShelterDog(chatId);
                                case "take" -> sendTakeMessage(chatId);
                                case "send" -> sendSendMessage(chatId);
                                case "help" -> sendHelpMessage(chatId);
                                case "contacts" -> contactData(chatId);
                            }

                            // Отправка подтверждения о выполнении команды
                            AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery(callbackQuery.id());
                            telegramBot.execute(answerCallbackQuery);
                        } else if (update.message() != null) {

                            // Обработка текстовых сообщений
                            Message message = update.message();
                            String text = message.text();
                            Long chatId = message.chat().id();

                            if (text.equals("/start")) {
                                sendStartMessage(chatId);
                            } else {
                                saveUserInfo(chatId, text);
//                            } else {
//                                sendDefaultMessage(chatId);
                            }
                        }
                    });
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void sendStartMessage(Long chatId) {
        InlineKeyboardMarkup inlinekeyboardMarkup = new InlineKeyboardMarkup(
                new InlineKeyboardButton("Кошки").callbackData("cats"),
                new InlineKeyboardButton("Собаки").callbackData("dogs"),
                new InlineKeyboardButton("Оставить контакты").callbackData("contacts"));
        SendMessage sendMessage = new SendMessage(chatId, "Привет! Я бот-помощник для поиска приюта для животных. " +
                "Я могу помочь тебе выбрать приют для кошек или для собак. Выбери, пожалуйста, один из приютов:");
        sendMessage.replyMarkup(inlinekeyboardMarkup);
        sendTelegramMessage(sendMessage);
    }

    private void sendCatsMenu(Long chatId) {
        InlineKeyboardMarkup inlinekeyboardMarkup = new InlineKeyboardMarkup(
                new InlineKeyboardButton("Узнать информацию о приюте").callbackData("info_cat"),
                new InlineKeyboardButton("Как взять животное из приюта").callbackData("take"),
                new InlineKeyboardButton("Прислать отчет о питомце").callbackData("send"),
                new InlineKeyboardButton("Позвать волонтера").callbackData("help"));
        SendMessage sendMessage = new SendMessage(chatId, "Выбери, пожалуйста, один из вариантов:");
        sendMessage.replyMarkup(inlinekeyboardMarkup);
        sendTelegramMessage(sendMessage);
    }

    private void sendDogsMenu(Long chatId) {
        InlineKeyboardMarkup inlinekeyboardMarkup = new InlineKeyboardMarkup(
                new InlineKeyboardButton("Узнать информацию о приюте").callbackData("info_dog"),
                new InlineKeyboardButton("Как взять животное из приюта").callbackData("take"),
                new InlineKeyboardButton("Прислать отчет о питомце").callbackData("send"),
                new InlineKeyboardButton("Позвать волонтера").callbackData("help"));
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
        SendMessage sendMessage = new SendMessage(chatId, "Итак, если вы хотите завести питомца и уверены, что хотите забрать его из приюта, то вот несколько простых шагов:\n" +
                "1. Удостоверьтесь, что у вас и у членов вашей семьи нет аллергии на животных.\n" +
                "2. Для того, что бы не тратить время на поиск животного в приюте, вы можете перед визитом ознакомится с некоторыми из них на нашем сайте или на волонтерских страничках в социальных сетях - Волонтерские странички приюта Дворняга - vkontakte\n" +
                "3. После этого можно приехать в приют и познакомиться с понравившимся животным. C собачкой лучшей выйти на прогулку на специально отведенной территории и познакомится там ближе с характером животного.\n" +
                "4. После того, как вы поймете, что именно эту  собачку вы хотите забрать из приюта, необходимо пообщаться с работником приюта или с курирующим волонтером, чтобы узнать про особенности поведения, содержания и питания животного, а также рассказать в каких условиях в дальнейшем будет жить питомец.\n" +
                "5. Следующий этап - это оформление договора в нем фиксируются данные обеих сторон, оговариваются пункты ответственного содержания животного.\n" +
                "6. На следующий день после подписания договора, вы можете приехать и забрать питомца домой. Для собачки следует привезти поводок с ошейником.");
        sendTelegramMessage(sendMessage);
    }

    private void sendSendMessage(Long chatId) {
        // Логика обработки кнопки /send
        SendMessage sendMessage = new SendMessage(chatId, "Прислать отчет о питомце");
        sendTelegramMessage(sendMessage);
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

    private void saveUserInfo(Long chatId, String text) {
        // Логика сохранения в БД
        String[] parts = text.split(" ");
        if (parts.length == 2) {
            String userName = parts[0];
            String userPhone = parts[1];
            AppUser appUser = new AppUser(chatId, userName, userPhone);
            userRepository.save(appUser);
            saveUserInfoMessage(chatId);
        }
    }

    private void saveUserInfoMessage(Long chatId) {
        // Логика обработки кнопки InfoMessage
        SendMessage sendMessage = new SendMessage(chatId, "Контактные данные сохранены");
        sendTelegramMessage(sendMessage);
    }

    private void sendDefaultMessage(Long chatId) {
        // Сообщение по дефолту
        SendMessage sendMessage = new SendMessage(chatId, "Вы указали неверную команду. " +
                "Пожалуйста, начните с выбора приюта");
        sendTelegramMessage(sendMessage);
        sendStartMessage(chatId);
    }


    private void sendTelegramMessage(SendMessage sendMessage) {
        SendResponse sendResponse = telegramBot.execute(sendMessage);
        if (!sendResponse.isOk()) {
            logger.error("Error during sending message: {}", sendResponse.description());
        }
    }
}
