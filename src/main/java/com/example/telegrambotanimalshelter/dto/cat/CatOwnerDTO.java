package com.example.telegrambotanimalshelter.dto.cat;

import com.example.telegrambotanimalshelter.entity.CatOwner;
import com.example.telegrambotanimalshelter.entity.enums.StatusPetOwner;
import java.time.LocalDate;

public record CatOwnerDTO(Long idCatOwner, Long chat_id, String fullName, String phone, String address, StatusPetOwner status, LocalDate finish, Long catId) {
    public static CatOwnerDTO catOwnerToDTO(CatOwner catOwner) {
        return new CatOwnerDTO(catOwner.idCatOwner(), catOwner.getChatId(), catOwner.getFullName(), catOwner.getPhone(), catOwner.getAddress(), catOwner.getStatusOwner(),catOwner.getFinish(), catOwner.getCat() != null ? catOwner.getCat().getId() : 0L);
    }
}
