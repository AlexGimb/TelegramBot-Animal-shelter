package com.example.telegrambotanimalshelter.service;
import com.example.telegrambotanimalshelter.StringValidation;
import com.example.telegrambotanimalshelter.dto.cat.ReportCatDTO;
import com.example.telegrambotanimalshelter.entity.AppUser;
import com.example.telegrambotanimalshelter.entity.CatOwner;
import com.example.telegrambotanimalshelter.entity.ReportCat;
import com.example.telegrambotanimalshelter.exception.NoSuchEntityIdException;
import com.example.telegrambotanimalshelter.exception.PetNoAssignedException;
import com.example.telegrambotanimalshelter.exception.ReportListIsEmptyException;
import com.example.telegrambotanimalshelter.repository.CatOwnerRepository;
import com.example.telegrambotanimalshelter.repository.CatRepository;
import com.example.telegrambotanimalshelter.repository.ReportCatRepository;
import com.example.telegrambotanimalshelter.repository.UserRepository;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.PhotoSize;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
public class ReportCatService {
    private ReportCatRepository reportCatRepository;
    private CatOwnerRepository catOwnerRepository;
    private CatRepository catRepository;

    private final UserRepository userRepository;
    private final TelegramBot telegramBot;


    @Value("${telegram.bot.token}")
    private String token;

    @Value("${service.file_info.uri}")
    private String fileInfoURI;
    @Value("${service.file_storage.uri}")
    private String fileStorageUri;

    public ReportCatService(ReportCatRepository reportCatRepository,
                            CatOwnerRepository catOwnerRepository,
                            CatRepository catRepository,
                            UserRepository userRepository,
                            TelegramBot telegramBot) {
        this.reportCatRepository = reportCatRepository;
        this.catOwnerRepository = catOwnerRepository;
        this.catRepository = catRepository;
        this.userRepository = userRepository;
        this.telegramBot = telegramBot;
    }

    private ReportCatDTO catReportToDTO(ReportCat report) {
        return new ReportCatDTO(
                report.getId(),
                report.getCat().getId(),
                report.getCatOwner().idCatOwner(),
                report.getCatChanges(),
                report.getReportDate());
    }

    /**
     * Добавить отчёт в базу (через бота)
     *
     * @param chatId,
     * @param catChanges,
     * @param photoAsBytesArray
     */
    public void addReport(Long chatId, String catChanges, byte[] photoAsBytesArray) {
        ReportCat report = new ReportCat();
        AppUser user = userRepository.findCatOwnerByChatId(chatId);
        CatOwner catOwner = catOwnerRepository.findCatOwnerByChatId(chatId);
        if (catOwner == null) {
            throw new NoSuchEntityIdException("Нет человека с таким chatId");
        }
        report.setCatOwner(catOwner);

        try {
            report.setCat(catOwner.getCat());
        } catch (NullPointerException e) {
            throw new PetNoAssignedException("У этого владельца еще нет питомца");
        }
        if (!StringValidation.validation(catChanges)) {
            throw new IllegalArgumentException();
        }
        if (photoAsBytesArray == null || photoAsBytesArray.length == 0) {
            throw new IllegalArgumentException("Failed to get report photo, check DB");
        }
        report.setCatChanges(catChanges);
        report.setReportDate(LocalDateTime.now());
        report.setPhotoAsArrayOfBytes(photoAsBytesArray);
        try {
            reportCatRepository.save(report);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        catReportToDTO(report);
    }

    /**
     * Получить отчёт по id отчёта
     *
     * @param id id отчёта
     * @return отчёт о питомце
     * @throws NoSuchEntityIdException попытке передать id несуществующего отчёта
     */
    public ReportCatDTO getReportByReportId(Long id) {
        try {
            return catReportToDTO(reportCatRepository.findById(id).orElseThrow());
        } catch (Exception e) {
            throw new NoSuchEntityIdException("Нет отчета с таким ID");
        }
    }

    public byte[] getReportPhotoBytesArray(Long id) {
        return reportCatRepository.findById(id).orElseThrow().getPhotoAsArrayOfBytes();
    }

    /**
     * Получить отчёт по id животного
     *
     * @param catId id питомца
     * @return список отчётов о питомце
     * @throws NoSuchEntityIdException при попытке передать id несуществующего животного
     */
    public List<ReportCatDTO> getReportsByPetId(Long catId) {

        List<ReportCatDTO> reportDTOS = reportCatRepository.findByCatId(catId)
                .stream()
                .map(this::catReportToDTO)
                .toList();
        if (!reportDTOS.isEmpty()) {
            return reportDTOS;
        } else {
            throw new NoSuchEntityIdException("Нет отчета с таким ID");
        }

    }

    /**
     * Получить список отчётов по дате отправки (по всем кошкам)
     *
     * @param localDateTime дата отправки отчётов
     * @return список отчётов на указанную дату
     * @throws NoSuchEntityIdException при попытке передать дату, в которую не существует отчётов
     */
    public List<ReportCatDTO> getReportsByDate(LocalDateTime localDateTime) {
        List<ReportCatDTO> reportDTOS = reportCatRepository
                .findAllByReportDate(localDateTime)
                .stream()
                .map(this::catReportToDTO).toList();
        if (!reportDTOS.isEmpty()) {
            return reportDTOS;
        } else {
            throw new NoSuchEntityIdException("Нет отчета с таким ID");
        }
    }

    /**
     * Получить список отчётов по дате отправки (для конкретной кошки)
     *
     * @param localDateTime дата отправки отчётов
     * @return список отчётов на указанную дату
     * @throws NoSuchEntityIdException при попытке передать дату, в которую не существует отчётов
     */
    public List<ReportCatDTO> getReportsByDateAndPetId(LocalDateTime localDateTime, Long catId) {
        List<ReportCatDTO> reportDTOS = reportCatRepository
                .findReportsByReportDateAndCatId(localDateTime, catId)
                .stream()
                .map(this::catReportToDTO).toList();
        if (!reportDTOS.isEmpty()) {
            return reportDTOS;
        } else {
            throw new NoSuchEntityIdException("Нет отчета с таким ID");
        }
    }

    /**
     * Получить все отчёты по chat ID
     *
     * @return список всех отчётов по кошкам
     * @throws ReportListIsEmptyException если в базе нет ни одного отчёта
     */
    public List<ReportCatDTO> getReportsByChatId(Long chatId) {
        List<ReportCatDTO> reports = reportCatRepository
                .findAll()
                .stream()
                .filter(r -> r.getCatOwner().getChatId().equals(chatId))
                .map(this::catReportToDTO)
                .toList();
        if (!reports.isEmpty()) {
            return reports;
        } else {
            throw new ReportListIsEmptyException("Список пустой, не содержит отчеты!");
        }
    }

    /**
     * Удалить отчёт по id отчёта
     *
     * @param reportId id отчёта
     * @throws NoSuchEntityIdException при попытке передать id несуществующего отчёта
     */
    public void deleteReportByReportId(Long reportId) {
        try {
            reportCatRepository.delete(reportCatRepository.findById(reportId).orElseThrow());
        } catch (Exception e) {
            throw new NoSuchEntityIdException("Нет отчета с таким ID");
        }
    }

    /**
     * Удалить отчёты по id животного
     *
     * @param catId id животного
     * @throws NoSuchEntityIdException при попытке передать id несуществующего животного
     */
    public void deleteReportsByPetId(Long catId) {
        List<ReportCat> reportDTOS = reportCatRepository.findByCatId(catId);
        if (reportDTOS.isEmpty()) {
            throw new NoSuchEntityIdException("No report with such pet Id");
        }
        reportCatRepository.deleteAllInBatch(reportCatRepository.findByCatId(catId));
    }

    private byte[] downloadFile(String filePath) {
        String fullURI = fileStorageUri.replace("{token}", token)
                .replace("{filePath}", filePath);
        URL urlObj;
        try {
            urlObj = new URL(fullURI);
        } catch (MalformedURLException e) {
            throw new RuntimeException();
        }

        try (InputStream is = urlObj.openStream()){
            return is.readAllBytes();
        }catch (IOException e){
            throw new RuntimeException();
        }
    }

    private ResponseEntity<String> getFilePath(String fileId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>(httpHeaders);
        return restTemplate.exchange(fileInfoURI, HttpMethod.GET, request, String.class, token, fileId);
    }

    public byte[] processAttachment(Message telegramMessage) {
        String fileId = "";
        if (telegramMessage.photo() != null) {
            fileId = Arrays.stream(telegramMessage.photo()).max(Comparator.comparing(PhotoSize::height)).orElseThrow().fileId();
        } else if (telegramMessage.document()!=null) {
            fileId = telegramMessage.document().fileId();
        }
        ResponseEntity<String> response = getFilePath(fileId);
        if (response.getStatusCode() == HttpStatus.OK) {
            JSONObject jsonObject = new JSONObject(response.getBody());
            String filePath = String.valueOf(jsonObject.getJSONObject("result").getString("file_path"));
            return downloadFile(filePath);
        } else {
            throw new RuntimeException("Bad response from TG service" + response);
        }
    }

}
