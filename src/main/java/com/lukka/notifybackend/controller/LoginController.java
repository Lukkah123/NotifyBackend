package com.lukka.notifybackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lukka.notifybackend.exception.ResourceNotFoundException;
import com.lukka.notifybackend.model.User;
import com.lukka.notifybackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

//@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/login")
public class LoginController implements WebMvcConfigurer {


    @Autowired
    UserService userService;

    private static final int COOKIEVALIDTIME = 100; // For how many seconds a cookie is valid
    private static HashMap<String, LocalDateTime> userCookies = new HashMap<>();

    /*
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }

     */

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
            return genCookie(user.getEmail());
        }
        return null;
    }

    // Logout
    @RequestMapping(value = "/", method = RequestMethod.DELETE, headers = "Accept=*/*", consumes = "application/json", produces = "application/json")
    public void logout(@RequestBody String params, HttpServletResponse response) throws IOException {
        Map<String, Object> result = paramsToMap(params);
        if (validateCookie(result, response)) {
            userCookies.remove(result.get("value"));
            response(response, 200, "Logged out");
        }
    }

    // To test if the cookie is still valid
    @RequestMapping(value = "/testCookie", method = RequestMethod.POST, headers = "Accept=*/*", consumes = "application/json", produces = "application/json")
    public void testCookie(@RequestBody String params, HttpServletResponse response) throws IOException {
        Map<String, Object> result = paramsToMap(params);
        validateCookie(result, response);
    }

    private static Cookie genCookie(String email) {
        Cookie jwtTokenCookie = new Cookie("userId", email);
        jwtTokenCookie.setHttpOnly(true);
        jwtTokenCookie.setDomain("localhost"); //Ändras från localhost om domän ändras!
        jwtTokenCookie.setPath("/");
        userCookies.put(email, LocalDateTime.now().plusSeconds(COOKIEVALIDTIME));
        return jwtTokenCookie;
    }

    public static boolean validateCookie(Map<String, Object> result, HttpServletResponse response) throws IOException {
        if (userCookies.get(result.get("value")) != null) {
            LocalDateTime userCookieTime = userCookies.get(result.get("value"));
            if (userCookieTime.isBefore(LocalDateTime.now())) {
                response(response, 400, "Session is no longer valid");
                userCookies.remove(result.get("value"));
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

    private Map<String, Object> paramsToMap(@RequestBody String params) throws JsonProcessingException {
        return new ObjectMapper().readValue(params, HashMap.class);
    }
}
