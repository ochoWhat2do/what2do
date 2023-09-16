package com.ocho.what2do;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling  //쓰지않을때 주석처리 스케줄러
public class What2doApplication {

	public static void main(String[] args) {
		SpringApplication.run(What2doApplication.class, args);
	}

}
