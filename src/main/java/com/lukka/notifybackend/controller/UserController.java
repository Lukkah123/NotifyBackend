package com.lukka.notifybackend.controller;

import com.lukka.notifybackend.model.User;
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

    @GetMapping("/getAll")
    public ResponseEntity<List<User>> getAllUsers(){
        try {
            return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getUser/{email}")
    public ResponseEntity<User> getUserById(@PathVariable String email) {
        return new ResponseEntity<>(userService.getUser(email), HttpStatus.OK);
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
    ResponseEntity<String> deleteUser(@PathVariable String email){
           try {
                userService.deleteUser(email);
                return new ResponseEntity<>(email + " successfully deleted." ,HttpStatus.OK);
           } catch (Exception e) {
               return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
           }
    }

    @DeleteMapping("/deleteAll")
    ResponseEntity<String> deleteAllUsers() {
        try {
            userService.deleteAllUsers();
            return new ResponseEntity<>("All users deleted successfully.",HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateUser/{email}")
    ResponseEntity<User> updateUser(@PathVariable("email") String email, @RequestBody User user) {
        try {
            return new ResponseEntity<>(userService.updateUser(user, email), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
