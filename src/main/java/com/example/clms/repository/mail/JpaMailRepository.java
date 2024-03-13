package com.example.clms.repository.mail;


import com.example.clms.entity.mail.Mail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaMailRepository extends JpaRepository<Mail, Integer>, MailRepository {

    Optional<Mail> findByEmail(String email);
    Mail save(Mail mail);
    void delete(Mail mail);
}
