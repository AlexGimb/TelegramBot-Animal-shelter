package com.example.telegrambotanimalshelter.listener;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
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
                        Long chat_id = message.chat().id();
                        String text = message.text();

                            switch (text) {
                                case "/start" -> sendMessage(chat_id, "Привет! Я бот-помощник для поиска приюта для животных. " +
                                        "Я могу помочь тебе выбрать приют для кошек или для собак. " +
                                        "Выбери, пожалуйста, один из вариантов: /cats или /dogs");

                                case "/cats" -> sendMessage(chat_id, """
                                        /info - Узнать информацию о приюте
                                        /take - Как взять животное из приюта
                                        /send - Прислать отчет о питомце
                                        /help - Позвать волонтера""");

                                case "/info" -> sendMessage(chat_id, """
                                        /general - Общая информация о приюте
                                        /address - Расписание работы приюта и адрес, схема проезда
                                        /security - Контактные данные охраны для оформления пропуска на машину
                                        /safety - Общие рекомендации о технике безопасности на территории приюта
                                        /contact - Оставить контактные данные для связи
                                        /other - Позвать волонтера""");

                                case "/general" -> sendMessage(chat_id, """
                                        Приют для кошек Мурёнка расположен на  территории, на которой размещены отдельные утепленные домики с летним выгулом для кошечек и хозяйственные пристройки, различного назначения.\s
                                        Имеется ветеринарный блок с карантинным помещением и изолятором. Все животные проходят вакцинацию против инфекционных заболеваний, опасных для человека, обработку против экто и эндопаразитов, стерилизации и кастрацию, также ежедневно осуществляется клинический осмотр животных.
                                        Кормят кошек  профессиональными  кормами, как сухими, так и жидкими в соответствии с рекомендациями ветеринарных врачей и физиологическими особенностями. За каждым животным закреплён специальный сотрудник по уходу, который персонально отвечает за них, расчёсывает, играет.                                                                                                                                                                                                                                            \s
                                                """);

                                case "/address" -> sendMessage(chat_id, """
                                        Мы находимся по адресу: г. Звенигород, улица Мурзика Полосатого, 125.
                                        Посещение приюта возможно:
                                        Понедельник– Четверг – с 11:00 до 18:00;
                                        Суббота — Воскресенье - с 11:00 до 20:00;
                                        Пятница – санитарный день;
                                        Прием животных в экстренных случаях - круглосуточно.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              \s
                                                """);

                                case "/security" -> sendMessage(chat_id, """
                                        Для оформления пропуска на транспортное средство необходимо отправить на электронную почту службы охраны (security205@mail.ru) сканы следующих документов:
                                        1. Документ, удостоверяющий личность (паспорт)\s
                                        2. Водительское удостоверение\s
                                        3. Паспорт транспортного средства                                                                                                                                                                                                                                                                                                                                                                                                                                           \s
                                                """);

                                case "/safety" -> sendMessage(chat_id, """
                                        ТЕХНИКА БЕЗОПАСНОСТИ
                                                                            
                                        1. Оградите территорию приюта высоким забором и убедитесь, что все ворота и двери на замках.
                                        Это поможет предотвратить случайные выходы животных на улицу и защитит их от внешних опасностей.
                                        2. Установите систему видеонаблюдения на территории приюта, чтобы убедиться, что никто не пытается проникнуть на территорию
                                        без разрешения.
                                        3. Обеспечьте достаточное освещение на территории приюта, чтобы снизить риск падений и других травм животных.
                                        4. Регулярно проверяйте здоровье животных, чтобы выявлять их болезни или травмы как можно раньше и немедленно предпринимать
                                        действия по лечению.
                                        5. Обеспечьте достаточное количество питьевой воды и корма для всех животных, чтобы избежать голода и обезвоживания.
                                        6.  Нанесите на шеи животных идентификационные ошейники с информацией о приюте и контактными данными для связи.
                                        7.Нанимайте квалифицированных сотрудников, которые будут заботиться о животных и проводить регулярные проверки на наличие
                                        травм и болезней.
                                        8. Разработайте и внедрите четкие процедуры по управлению животными в экстренных ситуациях, таких как пожар или эвакуация.
                                        9. Проводите регулярные уборки территории приюта, чтобы предотвратить распространение инфекций и заболеваний.
                                        10. Проводите тренировки сотрудников по правилам безопасности и требованиям по уходу за животными, чтобы они могли быстро и
                                        эффективно реагировать на любые ситуации.                                                                                                                                                                                                                                                                                                                                                                                                                                          \s
                                                """);

                                case "/contact" -> sendMessage(chat_id, """
                                        Напишите ваши контактные данные для связи                                                                                                                                                                                                                                                                                                                                                                                                                                         \s
                                                """);

                                case "/other" -> sendMessage(chat_id, """
                                        Если у вас остались вопросы, можете связаться с волонтерами по телефону +79871235647;                                                                                                                                                                                                                                                                                                                                                                                                                                           \s
                                                """);

                                case "/take" -> sendMessage(chat_id, """
                                        Как взять кошку из приюта? Прежде чем принимать такое серьезное решение, обговорите со своими близкими, готовы ли они тоже принять в свою семью нового питомца.\s
                                        Если у вас уже есть другие животные, убедитесь, что они подружатся с новым жильцом. Ваша семья вас поддерживает? Тогда самое время отправиться в наш приют для знакомства с будущим любимцем.
                                        Для того, что бы не тратить время на поиск животного в приюте, вы можете перед визитом ознакомится с некоторыми из них на нашем сайте или на волонтерских страничках в социальных сетях:
                                        •	Волонтерские странички приюта Мурёнка - facebook / instagram / vkontakte
                                        При выборе питомца учитывайте и свой образ жизни. Например, если вы постоянно работаете или много путешествуете, то лучше завести независимую кошку, не склонную к беспокойству или нервному поведению. Маленький котенок - не вариант, так как за ним нужно много ухаживать и заботиться.
                                        Домоседам подойдет ласковый и энергичный питомец, который сможет развлекать вас в свободное время. Если у вас есть дети, то будущий пушистый друг должен быть добрым и дружелюбным, чтобы выдержать капризы и игры ребят.
                                        После этого можно приехать в приют и  познакомиться с понравившимся животным. Кошек можно навестить прямо в домиках. Следующий этап - это оформление договора в нем фиксируются данные обеих сторон, оговариваются пункты ответственного содержания животного.
                                        На следующий день после подписания договора, вы можете приехать и забрать питомца домой. За кошечкой следует приехать с переноской.
                                        И не забудьте заранее узнать у сотрудников приюта, есть ли какие-то проблемы со здоровьем у вашего питомца. Часто у животных, живших на улице или переживших жестокое обращение, есть патологии, поэтому лучше сразу запастись необходимыми лекарствами.
                                                """);

                                case "/send" -> sendMessage(chat_id, "");

                                case "/help" -> sendMessage(chat_id, """
                                        Если у вас остались вопросы, можете связаться с волонтерами по телефону +79871235647;                                                                                                                                                                                                                                                                                                                                              
                                                """);

                                case "/dogs" -> sendMessage(chat_id, """
                                        /infoDog - Узнать информацию о приюте
                                        /takeDog - Как взять животное из приюта
                                        /sendDog - Прислать отчет о питомце
                                        /help - Позвать волонтера""");

                                case "/infoDog" -> sendMessage(chat_id, """
                                        /generalDog - Общая информация о приюте
                                        /addressDog - Расписание работы приюта и адрес, схема проезда
                                        /security - Контактные данные охраны для оформления пропуска на машину
                                        /safety - Общие рекомендации о технике безопасности на территории приюта
                                        /contact - Оставить контактные данные для связи
                                        /other - Позвать волонтера""");

                                case "/generalDog" -> sendMessage(chat_id, """
                                        Приют расположен на большой территории, на которой размещены утеплённые вольеры, выгульные площадки и хозяйственные пристройки.\s
                                        Имеется ветеринарный блок с карантинным помещением и изолятором. Все животные проходят вакцинацию против инфекционных заболеваний, опасных для человека, обработку против экто и эндопаразитов, стерилизацию и кастрацию, также ежедневно осуществляется клинический осмотр животных.\s
                                        Кормят собак профессиональными сухими кормами в соответствии с рекомендациями ветеринарных врачей и физиологическими особенностями. За каждым животным закреплён специальный сотрудник по уходу, который персонально отвечает за него, выгуливает, расчёсывает, играет, учит собак элементарным командам и ходить на поводке – адаптирует собак к будущей домашней жизни.                                                                                                                                                                                                                                            \s
                                                """);

                                case "/addressDog" -> sendMessage(chat_id, """
                                        Мы находимся по адресу: г. Красногорск, улица Шарика Мохнатова, 5.
                                        Посещение приюта возможно:
                                        Понедельник– Четверг – с 10:00 до 20:00;
                                        Суббота — Воскресенье - с 11:00 до 21:00;
                                        Пятница – санитарный день;
                                        Прием животных в экстренных случаях - круглосуточно.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              \s
                                                """);

                                case "/takeDog" -> sendMessage(chat_id, """
                                        Как забрать собаку из приюта:
                                        Итак, если вы хотите завести питомца и уверены, что хотите забрать его из приюта, то вот несколько простых шагов:
                                        1. Удостоверьтесь, что у вас и у членов вашей семьи нет аллергии на животных.
                                        2. Для того, что бы не тратить время на поиск животного в приюте, вы можете перед визитом ознакомится с некоторыми из них на нашем сайте или на волонтерских страничках в социальных сетях - Волонтерские странички приюта Дворняга - vkontakte
                                        3. После этого можно приехать в приют и познакомиться с понравившимся животным. C собачкой лучшей выйти на прогулку на специально отведенной территории и познакомится там ближе с характером животного.
                                        4. После того, как вы поймете, что именно эту  собачку вы хотите забрать из приюта, необходимо пообщаться с работником приюта или с курирующим волонтером, чтобы узнать про особенности поведения, содержания и питания животного, а также рассказать в каких условиях в дальнейшем будет жить питомец.
                                        5. Следующий этап - это оформление договора в нем фиксируются данные обеих сторон, оговариваются пункты ответственного содержания животного.
                                        6. На следующий день после подписания договора, вы можете приехать и забрать питомца домой. Для собачки следует привезти поводок с ошейником.
                                                """);

                                case "/sendDog" -> sendMessage(chat_id, " ");

                                default -> sendMessage(chat_id, "Вы указали неверную комманду. " +
                                        "Пожалуйста, начните с команды /start");
                            }
                    });
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
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
