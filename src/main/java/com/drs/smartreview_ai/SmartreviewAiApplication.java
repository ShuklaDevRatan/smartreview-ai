package com.drs.smartreview_ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching

public class SmartreviewAiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartreviewAiApplication.class, args);
	}

}
