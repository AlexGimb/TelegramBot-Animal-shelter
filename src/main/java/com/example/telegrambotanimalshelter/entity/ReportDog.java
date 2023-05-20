package com.example.telegrambotanimalshelter.entity;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "dog_reports")
public class ReportDog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(targetEntity = Dog.class)
    @JoinColumn(name = "dog_id", nullable = false)
    private Dog dog;
    @ManyToOne(targetEntity = DogOwner.class, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_dog_owner", nullable = false)
    /**Поле владельца животного, который пишет отчёт*/
    private DogOwner dogOwner;

    @Column(length = 2048)
    /**Поле с описанием рациона и режима питания, адаптации и состояния, а также изменений в поведении собаки*/
    private String dogChanges;
    @Column()
    /**Поле с датой и временем отправки отчёта*/
    private LocalDateTime reportDate;
    @Lob
    @Column(columnDefinition="BLOB")
    private byte[] photoAsArrayOfBytes;

    public ReportDog() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Dog getDog() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }

    public DogOwner getDogOwner() {
        return dogOwner;
    }

    public void setDogOwner(DogOwner dogOwner) {
        this.dogOwner = dogOwner;
    }

    public String getDogChanges() {
        return dogChanges;
    }

    public void setDogChanges(String dogChanges) {
        this.dogChanges = dogChanges;
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
