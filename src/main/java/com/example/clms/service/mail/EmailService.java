package com.example.clms.service.mail;

public interface EmailService {
    void sendEmail(String toEmail);
    void verifyAuthNum(String email, String authNum);
}
