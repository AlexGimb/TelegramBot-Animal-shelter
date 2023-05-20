package com.example.telegrambotanimalshelter.repository;
import com.example.telegrambotanimalshelter.entity.CatOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatOwnerRepository extends JpaRepository<CatOwner, Long> {
    CatOwner findCatOwnerByChatId(long chatId);
}
