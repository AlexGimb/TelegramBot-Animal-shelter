package com.example.telegrambotanimalshelter.repository;
import com.example.telegrambotanimalshelter.entity.ReportCat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReportCatRepository extends JpaRepository<ReportCat, Long>  {
        List<ReportCat> findByCatId(Long catId);

        List<ReportCat> findAllByReportDate(LocalDateTime reportDate);

        List<ReportCat> findReportsByReportDateAndCatId(LocalDate date, Long catId);
}
