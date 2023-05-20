package com.example.telegrambotanimalshelter.repository;
import com.example.telegrambotanimalshelter.entity.Cat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatRepository extends JpaRepository<Cat, Long> {
}
