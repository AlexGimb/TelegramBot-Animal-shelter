package com.example.telegrambotanimalshelter.service;
import com.example.telegrambotanimalshelter.entity.AppUser;
import org.springframework.stereotype.Service;
import com.example.telegrambotanimalshelter.repository.UserRepository;

@Service
public class UserService {
    public final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser (AppUser user) {
        userRepository.save(user);
    }
}

