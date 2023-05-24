package com.example.telegrambotanimalshelter.dto.cat;

import com.example.telegrambotanimalshelter.entity.Cat;
import com.example.telegrambotanimalshelter.entity.CatOwner;

import java.time.LocalDateTime;

public record ReportCatDTO (Long id, Long catId, Long catOwnerId, String catChanges, LocalDateTime reportDate) {
}
