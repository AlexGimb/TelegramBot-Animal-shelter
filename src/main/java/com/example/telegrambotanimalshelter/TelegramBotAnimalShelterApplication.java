package com.example.telegrambotanimalshelter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class TelegramBotAnimalShelterApplication {

	public static void main(String[] args) {
		SpringApplication.run(TelegramBotAnimalShelterApplication.class, args);
	}

}
