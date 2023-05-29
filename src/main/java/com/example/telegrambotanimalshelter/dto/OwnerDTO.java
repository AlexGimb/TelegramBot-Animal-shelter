package com.example.telegrambotanimalshelter.dto;

import com.example.telegrambotanimalshelter.entity.Owner;
import com.example.telegrambotanimalshelter.entity.enums.StatusPetOwner;
import java.time.LocalDate;
import java.util.Objects;

public record OwnerDTO(Long idOwner, Long chat_id, String fullName,
                       String phone, String address, StatusPetOwner status,
                       LocalDate finish, Long catId, Long dogId) {
    public static OwnerDTO catOwnerToDTO(Owner owner) {
        return new OwnerDTO(owner.idOwner(), owner.getChatId(), owner.getFullName(),
                owner.getPhone(), owner.getAddress(), owner.getStatusOwner(), owner.getFinish(),
                owner.getCat() != null ? owner.getCat().getId() : 0L,
                owner.getDog() != null ? owner.getDog().getId() : 0L);
    }

}
