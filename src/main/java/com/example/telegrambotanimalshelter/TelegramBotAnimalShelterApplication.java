package com.example.telegrambotanimalshelter;

import configuration.TelegramBotConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.io.IOException;


@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class TelegramBotAnimalShelterApplication {
	public static void main(String[] args) throws IOException, TelegramApiException {
		SpringApplication.run(TelegramBotAnimalShelterApplication.class, args); {

			//создание экземпляра класса
			TelegramBotConfiguration bot = new TelegramBotConfiguration();

			//запуск бота
			bot.start();
		}
	}
}
