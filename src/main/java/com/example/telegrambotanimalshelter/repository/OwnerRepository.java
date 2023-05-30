package com.example.telegrambotanimalshelter.repository;
import com.example.telegrambotanimalshelter.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {
    Owner findOwnerByChatId(long chatId);
}
