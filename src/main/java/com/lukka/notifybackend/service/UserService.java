package com.lukka.notifybackend.service;

import com.lukka.notifybackend.exception.EmptyRepositoryException;
import com.lukka.notifybackend.exception.ResourceNotFoundException;
import com.lukka.notifybackend.model.Note;
import com.lukka.notifybackend.model.User;
import com.lukka.notifybackend.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserService {


    private final UserRepo userRepo;
    private final NoteService noteService;
    @Autowired
    public UserService(UserRepo userRepo, NoteService noteService) {
        this.userRepo = userRepo;
        this.noteService = noteService;
    }

    public User save(User user) {
        return userRepo.save(user);
    }

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

    @Transactional
    public User updateUser(User user, String email) {
        User existingUser = userRepo.findById(email).orElseThrow(() ->
                new ResourceNotFoundException("User", "email", email));
        existingUser.setPassword(user.getPassword());
        return userRepo.save(existingUser);
    }

    @Transactional
    public User addNoteToUser(String email, Note note) {
        User user = getUser(email);
        user.addNote(note);
        return user;
    }

    @Transactional
    public Note removeNoteFromUser(String email, Long noteId) {
        User user = getUser(email);
        Note note = noteService.getNote(noteId);
        user.removeNote(note);
        return note;
    }
}
