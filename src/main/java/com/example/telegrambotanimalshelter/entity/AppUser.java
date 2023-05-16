package com.example.telegrambotanimalshelter.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
    @Entity
    @Table(name = "saved_users")
    public class AppUser {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;

        @Column(name = "phone", nullable = false)
        private String email;

        @Column(name = "chat_id", nullable = false)
        private long chatId;

        @Column(name = "time", nullable = false)
        private LocalDateTime time;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public long getChatId() {
            return chatId;
        }

        public void setChatId(long chatId) {
            this.chatId = chatId;
        }

        public LocalDateTime getNotificationDateTime() {
            return time;
        }

        public void setNotificationDateTime(LocalDateTime notificationDateTime) {
            this.time = notificationDateTime;
        }
    }

