package com.example.clms.service.mail;

import com.example.clms.common.exception.AuthNumNotEqualException;
import com.example.clms.common.exception.UserNotFoundException;
import com.example.clms.common.exception.ErrorCode;
import com.example.clms.entity.mail.Mail;
import com.example.clms.repository.mail.MailRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.Random;

@Service
@RequiredArgsConstructor
@SessionAttributes("AuthNumSessionDto")
public class EmailServiceImpl implements EmailService {

    //의존성 주입을 통해서 필요한 객체를 가져온다.
    private final JavaMailSender emailSender;
    private final MailRepository mailRepository;
    private String authNum; //랜덤 인증 코드

    //실제 메일 전송
    @Transactional
    public void sendEmail(String toEmail) {
        createCode();
        MimeMessage message = createEmailForm(toEmail);
        emailSender.send(message);
        saveAuthNum(toEmail, this.authNum);
    }

    // 인증번호 생성
    private void createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for(int i=0;i<8;i++) {
            int index = random.nextInt(3);

            switch (index) {
                case 0 :
                    key.append((char) ((int)random.nextInt(26) + 97));
                    break;
                case 1:
                    key.append((char) ((int)random.nextInt(26) + 65));
                    break;
                case 2:
                    key.append(random.nextInt(9));
                    break;
            }
        }
        authNum = key.toString();
    }

    //메일 작성
    private MimeMessage createEmailForm(String email) {
        String setFrom = "clmsmailservice@naver.com"; //email-config에 설정한 자신의 이메일 주소(보내는 사람)
        String title = "CLMS 회원가입 인증 번호"; //제목
        MimeMessage message = emailSender.createMimeMessage();

        System.out.println(email);
        try {
            message.addRecipients(MimeMessage.RecipientType.TO, email); //보낼 이메일 설정
            message.setSubject(title); //제목 설정
            message.setFrom(setFrom); //보내는 이메일
            message.setText("이메일 인증 코드 " + this.authNum);
        } catch (MessagingException me) {

        }
        return message;
    }

    private void saveAuthNum(String email, String authNum) {
        mailRepository.save(Mail.builder()
                .email(email)
                .authNum(authNum)
                .build());
    }

    public void verifyAuthNum(String email, String authNum) {
        Mail mail = mailRepository.findByEmail(email);

        if(mail == null) {
            throw new UserNotFoundException(ErrorCode.USER_NOT_FOUND);
        } else {
            compareAuthNum(authNum, mail.getAuthNum());
            mailRepository.delete(mail);
        }
    }

    private void compareAuthNum(String authNum, String dbAuthNum) {
        if (!dbAuthNum.equals(authNum)) {
            throw new AuthNumNotEqualException(ErrorCode.FAIL_VERIFY);
        }
    }
}
