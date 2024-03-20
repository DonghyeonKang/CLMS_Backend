package com.example.clms.service.user;

import com.example.clms.common.exception.ErrorCode;
import com.example.clms.common.exception.UserNotFoundException;
import com.example.clms.entity.user.User;
import com.example.clms.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component  // component scan 방식으로 빈 등록. Repository 해도 상관없음
@RequiredArgsConstructor // private 필드를 생성자 주입으로 DI 구현
public class UserServiceImpl implements UserService {  // 사용자 회원가입, 사용자 정보 불러오기

    private final UserRepository userRepository;

    // 회원 조회
    public User getUser(String email) {
        return userRepository.findByUsername(email)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    // 회원 탈퇴
    @Transactional
    public void deleteUser(String email) {
        userRepository.deleteByUsername(email);
    }

    // 비밀번호 재설정
    public void resetPassword(String username, String password) {
        User userToUpdate = getUser(username);

        userToUpdate.setPassword(password);
        userRepository.save(userToUpdate);
    }
}
