package com.redit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

import com.redit.config.SwaggerConfiguration;

//For Async processing do use EnableAsync
@SpringBootApplication
@EnableAsync
@Import(SwaggerConfiguration.class)
public class ReditApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReditApplication.class, args);
	}

}
