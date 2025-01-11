package com.senla.courses;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class CoursesApplication {

	private static final Logger logger = LoggerFactory.getLogger(CoursesApplication.class);


	public static void main(String[] args) {
		logger.info("Запустили logger в классе {}", CoursesApplication.class);
		SpringApplication.run(CoursesApplication.class, args);
	}

}
