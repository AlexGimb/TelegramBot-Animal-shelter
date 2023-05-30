package com.example.telegrambotanimalshelter.dto;

import com.example.telegrambotanimalshelter.entity.*;
import com.example.telegrambotanimalshelter.entity.enums.ColorOfPet;
import com.example.telegrambotanimalshelter.entity.enums.GenderOfPet;
import com.example.telegrambotanimalshelter.entity.enums.StatusOfPet;

import java.util.Objects;

public record DogDTO (Long id, String nickName, int birthYear, GenderOfPet gender,
                        ColorOfPet color, String species, String description,
                        StatusOfPet status, Long user) {

    public static DogDTO dogToDTO(Dog dog) {
        return new DogDTO(dog.getId(), dog.getNickName(), dog.getBirthYear(), dog.getGender(),
                dog.getColor(), dog.getSpecies(), dog.getDescription(), dog.getStatus(),
                dog.getOwner()!=null? dog.getOwner().idOwner() :0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DogDTO dogDTO = (DogDTO) o;
        return birthYear == dogDTO.birthYear && Objects.equals(id, dogDTO.id) && Objects.equals(nickName, dogDTO.nickName) && gender == dogDTO.gender && color == dogDTO.color && Objects.equals(species, dogDTO.species) && Objects.equals(description, dogDTO.description) && status == dogDTO.status && Objects.equals(user, dogDTO.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nickName, birthYear, gender, color, species, description, status, user);
    }
}
