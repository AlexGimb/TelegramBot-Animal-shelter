package com.example.telegrambotanimalshelter.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_report")
public class ReportUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_user", nullable = false)
    /**Поле владельца животного, который пишет отчёт*/
    private AppUser user;
    @Column(name = "changes", length = 2048, nullable = true)
    /**Поле с описанием рациона и режима питания, адаптации и состояния, а также изменений в поведении кошки*/
    private String catChanges;
    @Column(name = "report_date")
    /**Поле с датой и временем отправки отчёта*/
    private LocalDateTime reportDate;
    @Lob
    @Column(name = "photo", columnDefinition="bytea")
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] photoAsArrayOfBytes;

    public ReportUser() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
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
