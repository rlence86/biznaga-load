package com.ramonlence.social_biznaga;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class SocialBiznagaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocialBiznagaApplication.class, args);
	}

}
