package com.example.telegrambotanimalshelter.dto.dog;

import com.example.telegrambotanimalshelter.entity.DogOwner;
import com.example.telegrambotanimalshelter.entity.enums.StatusPetOwner;

import java.time.LocalDate;

public record DogOwnerDTO(Long idDogOwner, Long chat_id, String fullName, String phone, String address, StatusPetOwner status, LocalDate finish, Long dogId) {
    public static DogOwnerDTO dogOwnerToDTO(DogOwner dogOwner) {
        return new DogOwnerDTO(dogOwner.idDogOwner(), dogOwner.getChatId(), dogOwner.getFullName(), dogOwner.getPhone(), dogOwner.getAddress(), dogOwner.getStatusOwner(),dogOwner.getFinish(), dogOwner.getDog() != null ? dogOwner.getDog().getId() : 0L);
    }
}
