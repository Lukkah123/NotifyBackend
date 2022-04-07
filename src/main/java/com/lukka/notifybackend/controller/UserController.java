package com.lukka.notifybackend.controller;

import com.lukka.notifybackend.model.User;
import com.lukka.notifybackend.repo.UserRepo;
import com.lukka.notifybackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user){
        try {
            return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<User>> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @DeleteMapping("/deleteUser/{email}")
    void deleteUser(@PathVariable String email){
        userService.deleteUser(email);
    }

    @GetMapping("/test")
    public String test() {
        return "This is a test";
    }
}
