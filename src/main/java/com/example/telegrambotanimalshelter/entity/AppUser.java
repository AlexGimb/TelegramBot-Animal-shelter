package com.example.telegrambotanimalshelter.entity;

import javax.persistence.*;

    @Entity
    @Table(name = "saved_users")
    public class AppUser {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;

        @Column(name = "chat_id", nullable = false)
        private long chatId;

        @Column(name = "name", nullable = false)
        private String name;

        @Column(name = "phone", nullable = false)
        private String phone;

        public AppUser() {
        }

        public AppUser(long chatId, String name, String phone) {
            this.chatId = chatId;
            this.name = name;
            this.phone = phone;

        }


        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getChatId() {
            return chatId;
        }

        public void setChatId(long chatId) {
            this.chatId = chatId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setEmail(String email) {
            this.phone = email;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }



