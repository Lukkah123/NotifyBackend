package com.lukka.notifybackend.service;

import com.lukka.notifybackend.exception.ResourceNotFoundException;
import com.lukka.notifybackend.model.User;
import com.lukka.notifybackend.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public User getUser(String email) {
        Optional<User> userData = userRepo.findById(email);
        if (userData.isPresent())
            return userData.get();
        else
            throw new ResourceNotFoundException("User", "Email", email);
    }
}
