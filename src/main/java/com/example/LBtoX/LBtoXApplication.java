package com.example.LBtoX;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LBtoXApplication {

	public static void main(String[] args) {
		SpringApplication.run(LBtoXApplication.class, args);
		
	}

}
