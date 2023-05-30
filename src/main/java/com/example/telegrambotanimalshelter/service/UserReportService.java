package com.example.telegrambotanimalshelter.service;

import com.example.telegrambotanimalshelter.entity.Owner;
import com.example.telegrambotanimalshelter.entity.ReportOwner;
import com.example.telegrambotanimalshelter.exception.NoSuchEntityIdException;
import com.example.telegrambotanimalshelter.repository.*;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;


@Service
public class UserReportService {

    private final UserReportRepository userReportRepository;

    private final OwnerRepository ownerRepository;

    public UserReportService(UserReportRepository userReportRepository,
                             OwnerRepository ownerRepository) {
        this.userReportRepository = userReportRepository;
        this.ownerRepository = ownerRepository;
    }

    /**
     * Добавить отчёт в базу (через бот)
     *
     * @param chatId,
     * @param changes,
     */
    public void addReport(Long chatId, String changes, byte[] photoAsBytesArray) {
        ReportOwner report = new ReportOwner();
        Owner owner = ownerRepository.findOwnerByChatId(chatId);
        if (owner == null) {
            throw new NoSuchEntityIdException("Нет человека с таким chatId");
        }
        report.setOwner(owner);
        report.setCatChanges(changes);
        report.setReportDate(LocalDateTime.now());
        report.setPhotoAsArrayOfBytes(photoAsBytesArray);
        userReportRepository.save(report);
    }
    public static byte[] readPhotoBytes(String filePath) throws IOException {
        Path path = Path.of(filePath);
        return Files.readAllBytes(path);
    }

}
