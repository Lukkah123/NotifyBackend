package com.lukka.notifybackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lukka.notifybackend.exception.ResourceNotFoundException;
import com.lukka.notifybackend.model.User;
import com.lukka.notifybackend.repo.UserRepo;
import com.lukka.notifybackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/login")
public class LoginController implements WebMvcConfigurer {

    UserService userService;
    @Autowired
    UserRepo userRepo;
    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    private static final int COOKIEVALIDTIME = 100; // For how many seconds a cookie is valid
    private static HashMap<String, LocalDateTime> userCookies = new HashMap<>();

    // Login
    @PostMapping("/")
    public Cookie login(@RequestBody String params, HttpServletResponse response) throws IOException {
        Map<String, Object> result = paramsToMap(params);
        String email = result.get("email").toString(), password = result.get("password").toString();
        if (userCookies.containsKey(result.get("email"))) {
            response(response, 201, "Already logged in");
        } else {
            try {
                User user = userService.getUser(email);
                if (!user.getPassword().equals(password)) {
                    response(response, 401, "Error, please try again later");
                } else {
                    Cookie cookie = genCookie(email);
                    response.addCookie(cookie);
                    return cookie;
                }
            } catch (ResourceNotFoundException e) {
                response.setStatus(401);
                return null;
            }
        }
        response.setStatus(401);
        return null;
    }

    // Register
    @RequestMapping(value = "/", method = RequestMethod.PUT, headers = "Accept=*/*", consumes = "application/json", produces = "application/json")
    public Cookie register(@RequestBody User user, HttpServletResponse response) throws IOException {
        try {
            user = userService.getUser(user.getEmail());
            response(response, 401, "An account with that email already exists");
        } catch (ResourceNotFoundException e) {
            response.setStatus(200);
            userService.save(user); // Added this so that the user is actually saved in DB
            return genCookie(user.getEmail());
        }
        return null;
    }

    // Logout
    @RequestMapping(value = "/", method = RequestMethod.DELETE, headers = "Accept=*/*", consumes = "application/json", produces = "application/json")
    public void logout(@RequestBody String params, HttpServletResponse response) throws IOException {
        Map<String, Object> result = paramsToMap(params);
        Map<String, Object> cookie = paramsToMap(result.get("cookie").toString()); // Hugo kolla detta plox, lade till f??r att f?? det att funka
        if (validateCookie(result, response)) {
            System.out.println(result);
            System.out.println(cookie);
            userCookies.remove(cookie.get("value").toString());
            response(response, 200, "Logged out");
        }
    }

    @RequestMapping(value = "/validateCookie", method = RequestMethod.POST, headers = "Accept=*/*", consumes = "application/json", produces = "application/json")
    public void testCookie(@RequestBody String params, HttpServletResponse response) throws IOException {
        Map<String, Object> result = paramsToMap(params);
        validateCookie(result, response);
    }

    private static Cookie genCookie(String email) {
        Cookie jwtTokenCookie = new Cookie("userId", email);
        jwtTokenCookie.setHttpOnly(true);
        jwtTokenCookie.setDomain("localhost"); //??ndras fr??n localhost om dom??n ??ndras!
        jwtTokenCookie.setPath("/");
        userCookies.put(email, LocalDateTime.now().plusSeconds(COOKIEVALIDTIME));
        return jwtTokenCookie;
    }

    public static boolean validateCookie(Map<String, Object> result, HttpServletResponse response) throws IOException {
        Map<String, Object> cookie = paramsToMap(result.get("cookie").toString());
        String email = cookie.get("value").toString();
        if (userCookies.get(email) != null) {
            LocalDateTime userCookieTime = userCookies.get(email);
            if (userCookieTime.isBefore(LocalDateTime.now())) {
                response(response, 400, "Session is no longer valid");
                userCookies.remove(email);
            } else {
                response(response, 200, "Session is still valid");
                return true;
            }
        } else {
            response(response, 401, "Not logged in");
        }
        return false;
    }

    private static void response(HttpServletResponse response, int code, String message) throws IOException {
        response.setStatus(code);
        response.getOutputStream().println(message);
    }

    private static Map<String, Object> paramsToMap(@RequestBody String params) throws JsonProcessingException {
        return new ObjectMapper().readValue(params, HashMap.class);
    }
}
