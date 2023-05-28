package com.example.telegrambotanimalshelter.repository;
import com.example.telegrambotanimalshelter.entity.AppUser;
import com.example.telegrambotanimalshelter.entity.CatOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
    AppUser findCatOwnerByChatId(long chatId);

}

