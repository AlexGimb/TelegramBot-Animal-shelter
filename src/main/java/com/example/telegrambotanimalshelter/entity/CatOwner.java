package com.example.telegrambotanimalshelter.entity;
import com.example.telegrambotanimalshelter.entity.enums.StatusPetOwner;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

/** Класс потенциальных хозяев кошек, а также волонтёров, работающих с кошками.
 * При этом у волонтеров поде животного будет пустым. */
@Entity
@Table(name = "cat_owner")
public class CatOwner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cat_owner")
    private Long idCatOwner;
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

    /** Поле кошки, заполняется волонтером после передачи животного.
     * Правило: На испытательный срок - одно животное в одни руки.
     */
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cat_id")
    private Cat cat;

    /** Архив ежедневных отчетов потенциального хозяина кошки в порядке поступления. */
    @OneToMany(mappedBy = "catOwner", cascade = CascadeType.REMOVE)
    private List<ReportCat> reportCatList = new LinkedList<>();

    public CatOwner() {}

    public CatOwner(Long chatId, String fullName, String phone, String address, StatusPetOwner statusPetOwner) {
        this.chatId = chatId;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.statusPetOwner = statusPetOwner;
    }
    public CatOwner(Long chatId, String fullName, String phone, String address,
                    StatusPetOwner statusPetOwner, LocalDate finish, Cat cat) {
        this.chatId = chatId;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.statusPetOwner = statusPetOwner;
        this.finish = finish;
        this.cat = cat;
    }
    public CatOwner(Long idCatOwner, Long chatId, String fullName, String phone, String address,
                    StatusPetOwner statusPetOwner) {
        this.idCatOwner = idCatOwner;
        this.chatId = chatId;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.statusPetOwner = statusPetOwner;
    }
    public CatOwner(Long idCatOwner, Long chatId, String fullName, String phone, String address,
                    StatusPetOwner statusPetOwner, LocalDate finish, Cat cat) {
        this.idCatOwner = idCatOwner;
        this.chatId = chatId;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.statusPetOwner = statusPetOwner;
        this.finish = finish;
        this.cat = cat;
    }
//------------ Getters & setters -------------------------------------------------------

    public Long idCatOwner() {
        return idCatOwner;
    }

    public void idCatOwner(Long idCatOwner) {
        this.idCatOwner = idCatOwner;
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

    public Cat getCat() {
        return cat;
    }

    public void setCat(Cat cat) {
        this.cat = cat;
    }

    public List<ReportCat> getReportList() {
        return reportCatList;
    }

    public void setReportList(List<ReportCat> reportCatList) {
        this.reportCatList = reportCatList;
    }

    public LocalDate getFinish() {
        return finish;
    }

    public void setFinish(LocalDate finish) {
        this.finish = finish;
    }
}
