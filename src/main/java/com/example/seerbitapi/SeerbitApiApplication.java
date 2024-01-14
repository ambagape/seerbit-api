package com.example.seerbitapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@ComponentScans({
    @ComponentScan("com.example.seerbitapi")
})
@SpringBootApplication
public class SeerbitApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeerbitApiApplication.class, args);
    }
}
