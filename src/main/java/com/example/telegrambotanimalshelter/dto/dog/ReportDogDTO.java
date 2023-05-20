package com.example.telegrambotanimalshelter.dto.dog;
import com.example.telegrambotanimalshelter.entity.CatOwner;
import com.example.telegrambotanimalshelter.entity.Dog;
import java.time.LocalDateTime;

public record ReportDogDTO(Long id, Dog dog, CatOwner catOwner, String catChanges, LocalDateTime reportDate) {
}
