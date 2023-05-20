package com.example.telegrambotanimalshelter.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cat_reports")
public class ReportCat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(targetEntity = Dog.class)
    @JoinColumn(name = "cat_id", nullable = false)
    private Cat cat;
    @ManyToOne(targetEntity = DogOwner.class, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_cat_owner", nullable = false)
    /**Поле владельца животного, который пишет отчёт*/
    private CatOwner catOwner;

    @Column(length = 2048)
    /**Поле с описанием рациона и режима питания, адаптации и состояния, а также изменений в поведении кошки*/
    private String catChanges;
    @Column()
    /**Поле с датой и временем отправки отчёта*/
    private LocalDateTime reportDate;
    @Lob
    @Column(columnDefinition="BLOB")
    private byte[] photoAsArrayOfBytes;

    public ReportCat() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Cat getCat() {
        return cat;
    }

    public void setCat(Cat cat) {
        this.cat = cat;
    }

    public CatOwner getCatOwner() {
        return catOwner;
    }

    public void setCatOwner(CatOwner catOwner) {
        this.catOwner = catOwner;
    }

    public String getCatChanges() {
        return catChanges;
    }

    public void setCatChanges(String catChanges) {
        this.catChanges = catChanges;
    }

    public LocalDateTime getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDateTime reportDate) {
        this.reportDate = reportDate;
    }

    public byte[] getPhotoAsArrayOfBytes() {
        return photoAsArrayOfBytes;
    }

    public void setPhotoAsArrayOfBytes(byte[] photoAsArrayOfBytes) {
        this.photoAsArrayOfBytes = photoAsArrayOfBytes;
    }
}
