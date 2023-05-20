package com.example.telegrambotanimalshelter.entity;

import com.example.telegrambotanimalshelter.entity.enums.ColorOfPet;
import com.example.telegrambotanimalshelter.entity.enums.GenderOfPet;
import com.example.telegrambotanimalshelter.entity.enums.StatusOfPet;

import javax.persistence.*;

@Entity
@Table (name = "cat")
public class Cat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nick_name")
    private String nickName;
    @Column(name = "birth_year")
    private int birthYear;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private GenderOfPet gender;

    @Column(name = "color")
    @Enumerated(EnumType.STRING)
    private ColorOfPet color;

    @Column(name = "species")
    private String species;

    @Column(name = "description")
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusOfPet status;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cat_owner")
    private CatOwner catOwner;

    public Cat() {
    }

    public Cat(long id, String nickName, int birthYear, GenderOfPet gender, ColorOfPet color, String species, String description,
               StatusOfPet status, CatOwner catOwner) {
        this.id = id;
        this.nickName = nickName;
        this.birthYear = birthYear;
        this.gender = gender;
        this.color = color;
        this.species = species;
        this.description = description;
        this.status = status;
        this.catOwner = catOwner;
    }
    public Cat(long id, String nickName, int birthYear, GenderOfPet gender, ColorOfPet color, String species, String description,
               StatusOfPet status) {
        this.id = id;
        this.nickName = nickName;
        this.birthYear = birthYear;
        this.gender = gender;
        this.color = color;
        this.species = species;
        this.description = description;
        this.status = status;
    }

    public Cat(String nickName, int birthYear, GenderOfPet gender, ColorOfPet color, String species, String description,
               StatusOfPet status) {
        this.nickName = nickName;
        this.birthYear = birthYear;
        this.gender = gender;
        this.color = color;
        this.species = species;
        this.description = description;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public GenderOfPet getGender() {
        return gender;
    }

    public void setGender(GenderOfPet gender) {
        this.gender = gender;
    }

    public ColorOfPet getColor() {
        return color;
    }

    public void setColor(ColorOfPet color) {
        this.color = color;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StatusOfPet getStatus() {
        return status;
    }

    public void setStatus(StatusOfPet status) {
        this.status = status;
    }

    public CatOwner getCatOwner() {
        return catOwner;
    }

    public void setCatOwner(CatOwner catOwner) {
        this.catOwner = catOwner;
    }
}
