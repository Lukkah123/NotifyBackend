package com.lukka.notifybackend.controller;

import com.lukka.notifybackend.model.User;
import com.lukka.notifybackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/getAll")
    public ResponseEntity<List<User>> getAllUsers(){
        try {
            List<User> users = userService.getAllUsers();
            if (users.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            else
             return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getUser/{email}")
    public ResponseEntity<User> getUserById(@PathVariable String email) {
        try {
            Optional<User> userData = userService.getUser(email);
            return userData.map(user -> new ResponseEntity<>(user, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/test")
    public String test() {
        return "This is a test from Simon";
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user){
        try {
            return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED);
        }catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteUser/{email}")
    ResponseEntity<HttpStatus> deleteUser(@PathVariable String email){
           try {
                userService.deleteUser(email);
                return new ResponseEntity<>(HttpStatus.OK);
           } catch (Exception e) {
               return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
           }
    }

    @DeleteMapping("/deleteAll")
    ResponseEntity<HttpStatus> deleteAllUsers() {
        try {
            userService.deleteAllUsers();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateUserPassword/{email}")
    ResponseEntity<User> updateUserPassword(@PathVariable String email, @RequestBody String password) {
        try {
            Optional<User> userData = userService.getUser(email);
            if (userData.isPresent()) {
                return new ResponseEntity<>(userService.updateUserPassword(password, email), HttpStatus.OK);
            }
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateUser/{email}")
    ResponseEntity<User> updateUser(@PathVariable String email, @RequestBody User user) {
        try {
            Optional<User> userData = userService.getUser(email);
            if (userData.isPresent())
                return new ResponseEntity<>(userService.updateUser(user, email), HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
