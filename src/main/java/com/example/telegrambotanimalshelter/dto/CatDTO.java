package com.example.telegrambotanimalshelter.dto;
import com.example.telegrambotanimalshelter.entity.Cat;
import com.example.telegrambotanimalshelter.entity.enums.ColorOfPet;
import com.example.telegrambotanimalshelter.entity.enums.GenderOfPet;
import com.example.telegrambotanimalshelter.entity.enums.StatusOfPet;

import java.util.Objects;

public record CatDTO (Long id, String nickName, int birthYear, GenderOfPet gender,
                        ColorOfPet color, String species, String description,
                        StatusOfPet status, Long user) {

    public static CatDTO catToDTO(Cat cat) {
        return new CatDTO(cat.getId(), cat.getNickName(), cat.getBirthYear(), cat.getGender(),
                cat.getColor(), cat.getSpecies(), cat.getDescription(), cat.getStatus(),
                cat.getOwner() != null ? cat.getOwner().idOwner() : 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CatDTO catDTO = (CatDTO) o;
        return birthYear == catDTO.birthYear && Objects.equals(id, catDTO.id) && Objects.equals(nickName, catDTO.nickName) && gender == catDTO.gender && color == catDTO.color && Objects.equals(species, catDTO.species) && Objects.equals(description, catDTO.description) && status == catDTO.status && Objects.equals(user, catDTO.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nickName, birthYear, gender, color, species, description, status, user);
    }
}