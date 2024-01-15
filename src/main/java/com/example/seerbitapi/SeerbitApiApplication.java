package com.example.seerbitapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScans({
    @ComponentScan("com.example.seerbitapi")
})
@EnableScheduling
@SpringBootApplication
public class SeerbitApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeerbitApiApplication.class, args);
    }
}
