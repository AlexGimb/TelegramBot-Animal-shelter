package com.example.telegrambotanimalshelter.repository;
import com.example.telegrambotanimalshelter.entity.DogOwner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DogOwnerRepository extends JpaRepository<DogOwner, Long> {
    DogOwner findDogOwnerByChatId(long chatId);
}
