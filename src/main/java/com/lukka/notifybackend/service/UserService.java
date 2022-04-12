package com.lukka.notifybackend.service;

import com.lukka.notifybackend.exception.EmptyRepositoryException;
import com.lukka.notifybackend.exception.ResourceNotFoundException;
import com.lukka.notifybackend.model.User;
import com.lukka.notifybackend.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {


    private final UserRepo userRepo;
    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    // POST -----------------------------------------------------
    public User save(User user) {
        return userRepo.save(user);
    }
    // ----------------------------------------------------------

    // GET ------------------------------------------------------
    public List<User> getAllUsers() {
        List<User> users = userRepo.findAll();
        if (!users.isEmpty())
            return users;
        else
            throw new EmptyRepositoryException("GET", "User");
    }

    public User getUser(String email) {
        return userRepo.findById(email).orElseThrow(() ->
                new ResourceNotFoundException("User", "email", email));
    }

    // ---------------------------------------------------------

    // DELETE --------------------------------------------------
    public void deleteUser(String email) {
        userRepo.findById(email).orElseThrow(() ->
                new ResourceNotFoundException("User", "email", email));
        userRepo.deleteById(email);
    }

    public void deleteAllUsers() {
        List<User> users = userRepo.findAll();
        if (!users.isEmpty())
            userRepo.deleteAll();
        else throw new EmptyRepositoryException("DELETE", "User");
    }
    //---------------------------------------------------------

    // PUT ----------------------------------------------------
    public User updateUser(User user, String email) {
        User existingUser = userRepo.findById(email).orElseThrow(() ->
                new ResourceNotFoundException("User", "email", email));
        existingUser.setPassword(user.getPassword());
        return userRepo.save(existingUser);
    }
    // ---------------------------------------------------------
}
