package com.example.telegrambotanimalshelter.entity;
import com.example.telegrambotanimalshelter.entity.enums.StatusPetOwner;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

/** Класс потенциальных хозяев кошек, а также волонтёров, работающих с кошками.
 * При этом у волонтеров поде животного будет пустым. */
@Entity
@Table(name = "owner")
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "chat_id",nullable = false)
    private Long chatId;
    @Column(name = "full_name",nullable = false)
    private String fullName;
    @Column(name = "phone",nullable = false)
    private String phone;
    @Column(name = "address")
    private String address;
    @Column(name = "status", columnDefinition = "varchar(255) default 'SEARCH'")
    @Enumerated(EnumType.STRING)
    @ColumnDefault("SEARCH")
    private StatusPetOwner statusPetOwner;
    /** Дата окончания испытательного периода для потенциального хозяина животного.
     */
    @Column(name = "finish")
    private LocalDate finish;

    /** Поле кошки, заполняется волонтером после передачи животного.
     * Правило: На испытательный срок - одно животное в одни руки.
     */

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cat_id")
    private Cat cat;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dog_id")
    private Dog dog;

    /** Архив ежедневных отчетов потенциального хозяина кошки в порядке поступления. */
    @OneToMany(mappedBy = "owner", cascade = CascadeType.REMOVE)
    private List<ReportOwner> reportList = new LinkedList<>();

    public Owner() {

    }

    public Owner(Long chatId, String fullName, String phone) {
        this.chatId = chatId;
        this.fullName = fullName;
        this.phone = phone;
    }

    public Owner(Long chatId, String fullName, String phone, String address, StatusPetOwner statusPetOwner) {
        this.chatId = chatId;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.statusPetOwner = statusPetOwner;
    }
    public Owner(Long chatId, String fullName, String phone, String address,
                 StatusPetOwner statusPetOwner, LocalDate finish, Cat cat) {
        this.chatId = chatId;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.statusPetOwner = statusPetOwner;
        this.finish = finish;
        this.cat = cat;
    }

    public Owner(Long chatId, String fullName, String phone, String address,
                 StatusPetOwner statusPetOwner, LocalDate finish, Dog dog) {
        this.chatId = chatId;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.statusPetOwner = statusPetOwner;
        this.finish = finish;
        this.dog = dog;
    }

    public Owner(Long id, Long chatId, String fullName, String phone, String address,
                 StatusPetOwner statusPetOwner) {
        this.id = id;
        this.chatId = chatId;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.statusPetOwner = statusPetOwner;
    }
    public Owner(Long idOwner, Long chatId, String fullName, String phone, String address,
                 StatusPetOwner statusPetOwner, LocalDate finish, Cat cat) {
        this.id = idOwner;
        this.chatId = chatId;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.statusPetOwner = statusPetOwner;
        this.finish = finish;
        this.cat = cat;
    }

    public Owner(Long idOwner, Long chatId, String fullName, String phone, String address,
                 StatusPetOwner statusPetOwner, LocalDate finish, Dog dog) {
        this.id = idOwner;
        this.chatId = chatId;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.statusPetOwner = statusPetOwner;
        this.finish = finish;
        this.dog = dog;
    }
//------------ Getters & setters -------------------------------------------------------

    public Long idOwner() {
        return id;
    }

    public void idOwner(Long idCatOwner) {
        this.id = idCatOwner;
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

    public List<ReportOwner> getReportList() {
        return reportList;
    }

    public void setReportList(List<ReportOwner> reportCatList) {
        this.reportList = reportCatList;
    }

    public LocalDate getFinish() {
        return finish;
    }

    public void setFinish(LocalDate finish) {
        this.finish = finish;
    }

    public Cat getCat() {
        return cat;
    }

    public void setCat(Cat cat) {
        this.cat = cat;
    }

    public Dog getDog() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }
}
