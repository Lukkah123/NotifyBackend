package com.lukka.notifybackend;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEncryptableProperties
public class NotifyBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotifyBackendApplication.class, args);
    }
}
