package com.example.telegrambotanimalshelter.repository;

import com.example.telegrambotanimalshelter.entity.ReportOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserReportRepository extends JpaRepository<ReportOwner, Long> {

}