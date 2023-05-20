package com.example.telegrambotanimalshelter.repository;
import com.example.telegrambotanimalshelter.entity.ReportDog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReportDogRepository extends JpaRepository<ReportDog, Long> {
    List<ReportDog> findByDogId(Long dogId);

    List<ReportDog> findAllByReportDate(LocalDateTime reportDate);

    List<ReportDog> findReportsByReportDateAndDogId(LocalDate date, Long dogId);
}
