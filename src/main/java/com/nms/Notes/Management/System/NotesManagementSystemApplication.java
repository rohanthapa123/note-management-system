package com.nms.Notes.Management.System;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class NotesManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotesManagementSystemApplication.class, args);
	}

}
