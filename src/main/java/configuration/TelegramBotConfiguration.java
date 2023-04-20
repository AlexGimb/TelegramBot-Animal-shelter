package configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.DeleteMyCommands;
import lombok.SneakyThrows;
import model.AnimalShelterBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
public class TelegramBotConfiguration {
    private static final String CONFIG_FILE = "C:\\Users\\Alex\\IdeaProjects\\TelegramBot-Animal-shelter\\src\\main\\resources\\application.properties";
    private static final String TELEGRAM_BOT_TOKEN = "bot.token";
    private static final String TELEGRAM_BOT_NAME = "bot.username";
    private Properties props;

    public TelegramBotConfiguration() throws IOException {
        props = new Properties();
        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            props.load(input);
        }
    }

    // метод запуска бота
    @SneakyThrows
    public void start() {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        AnimalShelterBot bot = new AnimalShelterBot();
        botsApi.registerBot(bot);
    }

    //получение токена бота
    public String getTelegramBotToken() {
        return props.getProperty(TELEGRAM_BOT_TOKEN);
    }

    //получение имени бота
    public String getTelegramBotName() {
        return props.getProperty(TELEGRAM_BOT_NAME);
    }
}
