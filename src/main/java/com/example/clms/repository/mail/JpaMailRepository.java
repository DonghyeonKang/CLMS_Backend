package com.example.clms.repository.mail;


import com.example.clms.entity.mail.Mail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMailRepository extends JpaRepository<Mail, Integer>, MailRepository {

    Mail findByEmail(String email);
    Mail save(Mail mail);
    void delete(Mail mail);
}
