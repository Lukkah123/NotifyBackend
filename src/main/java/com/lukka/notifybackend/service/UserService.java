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
    // POST
    public User save(User user){
        return userRepo.save(user);
    }
    // GET
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public Optional<User> getUser(String email) {
        return userRepo.findById(email);
    }
    // DELETE
    public void deleteUser(String email) {
        userRepo.deleteById(email);
    }
    public void deleteAllUsers() {
        userRepo.deleteAll();
    }
    // PUT
    public User updateUserPassword(String password, String email) {
        Optional<User> userData = userRepo.findById(email);
        User updatedUser = userData.get();
        updatedUser.setPassword(password);
        return userRepo.save(updatedUser);
    }

    public User updateUser(User user, String email) {
        Optional<User> userData = userRepo.findById(email);
        User updatedUser = userData.get();
        updatedUser.setEmail(user.getEmail());
        updatedUser.setPassword(user.getPassword());
        return userRepo.save(updatedUser);
    }
}
