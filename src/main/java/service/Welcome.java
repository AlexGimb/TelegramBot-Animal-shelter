package service;

import model.AnimalShelterBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class Welcome {
    public static void main(String[] args) throws TelegramApiException, IOException {
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Введите Ваше имя:");
//        String name = scanner.nextLine();
//        System.out.println("Здравствуйте " + name);

        // связь с конфигурационным файлом
        Properties props = new Properties();
        props.load(new FileInputStream("C:\\Users\\Alex\\IdeaProjects\\TelegramBot-Animal-shelter\\src\\main\\resources\\ config.properties"));

        String username = props.getProperty("bot.username");
        String token = props.getProperty("bot.token");

        // создание экземпляра бота и связь с Telegram API
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(new AnimalShelterBot(username, token));

    }
}
