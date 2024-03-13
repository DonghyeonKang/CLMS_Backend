package com.example.clms.repository.mail;

import com.example.clms.entity.mail.Mail;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MailRepository {
    Optional<Mail> findByEmail(String email);
    Mail save(Mail mail);
    void delete(Mail mail);
}
