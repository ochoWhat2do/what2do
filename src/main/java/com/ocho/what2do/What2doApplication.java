package com.ocho.what2do;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableScheduling  //쓰지않을때 주석처리 배치프로그램
public class What2doApplication {

	public static void main(String[] args) {
		SpringApplication.run(What2doApplication.class, args);
	}

}
