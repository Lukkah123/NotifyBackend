package com.lukka.notifybackend.service;

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

    public User save(User user){
        return userRepo.save(user);
    }

    public List<User> getAllUsers() {
        List<User> users = userRepo.findAll();
        if (users.size() > 1)
            return users;
        else
        return null;
    }

    public Optional<User> getUser(String email) {
        return userRepo.findById(email);
    }

    public void deleteUser(String email) {
        userRepo.deleteById(email);
    }

    public void updateUserPassword(User user) {
        User userToUpdate = userRepo.getById(user.getEmail());
        userToUpdate.setPassword(user.getPassword());
        userRepo.save(userToUpdate);
    }
}
