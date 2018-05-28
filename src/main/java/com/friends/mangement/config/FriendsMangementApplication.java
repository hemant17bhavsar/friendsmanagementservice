package com.friends.mangement.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com.friends.mangement" })
public class FriendsMangementApplication {

	public static void main(String[] args) {
		SpringApplication.run(FriendsMangementApplication.class, args);
	}
}
