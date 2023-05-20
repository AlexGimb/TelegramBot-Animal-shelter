package com.example.telegrambotanimalshelter.entity;

import com.example.telegrambotanimalshelter.entity.enums.StatusPetOwner;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

/** Класс потенциальных хозяев собак, а также волонтёров, работающих с собаками.
 * При этом у волонтеров поде животного будет пустым. */
@Entity
@Table(name = "dog_owner")
public class DogOwner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dog_owner")
    private Long idDogOwner;
    @Column(name = "chat_id",nullable = false)
    private Long chatId;
    @Column(name = "full_name",nullable = false)
    private String fullName;
    @Column(name = "phone",nullable = false)
    private String phone;
    @Column(name = "address",nullable = false)
    private String address;
    @Column(name = "status",nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusPetOwner statusPetOwner;
    /** Дата окончания испытательного периода для потенциального хозяина животного.
     */
    @Column(name = "finish")
    private LocalDate finish;

    /** Поле собаки, заполняется волонтером после передачи животного.
     * Правило: На испытательный срок - одно животное в одни руки.
     */
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dog_id")
    private Dog dog;

    /** Архив ежедневных отчетов потенциального хозяина собаки в порядке поступления. */
    @OneToMany(mappedBy = "dogOwner", cascade = CascadeType.REMOVE)
    private List<ReportDog> reportDogList = new LinkedList<>();

// --------------------- Constructors ---------------------------------------------------

    /** Пустой конструктор, что бы Hibernate мог осуществлять манипуляции с классом. */
    public DogOwner() {}

    public DogOwner(Long chatId, String fullName, String phone, String address, StatusPetOwner statusPetOwner) {
        this.chatId = chatId;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.statusPetOwner = statusPetOwner;
    }
    public DogOwner(Long chatId, String fullName, String phone, String address,
                    StatusPetOwner statusPetOwner, LocalDate finish, Dog dog) {
        this.chatId = chatId;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.statusPetOwner = statusPetOwner;
        this.finish = finish;
        this.dog = dog;
    }
    public DogOwner(Long idDogOwner, Long chatId, String fullName, String phone, String address,
                    StatusPetOwner statusPetOwner) {
        this.idDogOwner = idDogOwner;
        this.chatId = chatId;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.statusPetOwner = statusPetOwner;
    }
    public DogOwner(Long idDogOwner, Long chatId, String fullName, String phone, String address,
                    StatusPetOwner statusPetOwner, LocalDate finish, Dog dog) {
        this.idDogOwner = idDogOwner;
        this.chatId = chatId;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.statusPetOwner = statusPetOwner;
        this.finish = finish;
        this.dog = dog;
    }
//------------ Getters & setters -------------------------------------------------------

    public Long idDogOwner() {
        return idDogOwner;
    }

    public void setIdDogOwner(Long idDogOwner) {
        this.idDogOwner = idDogOwner;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public StatusPetOwner getStatusOwner() {
        return statusPetOwner;
    }

    public void setStatusOwner(StatusPetOwner statusPetOwner) {
        this.statusPetOwner = statusPetOwner;
    }

    public Dog getDog() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }

    public List<ReportDog> getReportList() {
        return reportDogList;
    }

    public void setReportList(List<ReportDog> reportDogList) {
        this.reportDogList = reportDogList;
    }

    public LocalDate getFinish() {
        return finish;
    }

    public void setFinish(LocalDate finish) {
        this.finish = finish;
    }
}