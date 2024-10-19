package com.example.uberapp.UberApp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.uberapp.UberApp.services.EmailSenderService;

@SpringBootTest
class UberAppApplicationTests {

	@Autowired
	private EmailSenderService emailSenderService;

	@Test
	void contextLoads() {
		emailSenderService.sendEmail("jahayac327@abaot.com", "Subject", "This is body");

	}





}
