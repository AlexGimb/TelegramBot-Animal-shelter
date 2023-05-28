package com.example.telegrambotanimalshelter.repository;

import com.example.telegrambotanimalshelter.entity.AppUser;
import com.example.telegrambotanimalshelter.entity.ReportCat;
import com.example.telegrambotanimalshelter.entity.ReportUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserReportRepository extends JpaRepository<ReportUser, Long> {

}