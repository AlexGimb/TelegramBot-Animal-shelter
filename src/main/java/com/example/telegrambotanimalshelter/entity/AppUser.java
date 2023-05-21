package com.example.telegrambotanimalshelter.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
    @Entity
    @Table(name = "saved_users")
    public class AppUser {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;

        @Column(name = "chat_id", nullable = false)
        private Long chatId;

        @Column(name = "name", nullable = false)
        private String userName;

        @Column(name = "phone", nullable = false)
        private String userPhone;

        public AppUser(Long chatId, String userName, String userPhone) {
            this.chatId = chatId;
            this.userName = userName;
            this.userPhone = userPhone;
        }

        public AppUser() {

        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public Long getChatId() {
            return chatId;
        }

        public void setChatId(Long chatId) {
            this.chatId = chatId;
        }

        public String getUserPhone() {
            return userPhone;
        }

        public void setUserPhone(String userPhone) {
            this.userPhone = userPhone;
        }
    }

