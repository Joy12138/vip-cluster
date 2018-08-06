package com.lz.vip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

@ComponentScan("com.lz.vip")
@SpringBootApplication
@ImportResource("classpath:spring/spring-jdbc.xml")
public class Application{

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

	}

}
