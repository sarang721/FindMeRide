package com.example.uberapp.UberApp.services;

import org.springframework.stereotype.Service;

@Service
public interface EmailSenderService {

    void sendEmail(String toEmail, String subject, String body);

    void sendEmail(String toEmail[], String subject, String body);

}
