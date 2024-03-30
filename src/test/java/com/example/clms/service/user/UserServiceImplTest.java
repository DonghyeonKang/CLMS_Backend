package com.example.clms.service.user;

import com.example.clms.common.exception.UserNotFoundException;
import com.example.clms.entity.user.User;
import com.example.clms.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository);
    }
    @Test
    void 회원조회() {
        // given
        String email = "donghyeon09@naver.com";
        User returnUser = User.builder()
                .username("donghyeon09@naver.com")
                .build();
        when(userRepository.findByUsername(email)).thenReturn(Optional.ofNullable(returnUser));

        // when
        User user = userService.getUser(email);

        // then
        assertThat(returnUser).isEqualTo(user);
    }

    @Test
    void 회원조회_NULL회원() {
        // given
        String email = "donghyeon09@naver.com";
        User returnUser = User.builder()
                .username("donghyeon09@naver.com")
                .build();
        when(userRepository.findByUsername(email)).thenReturn(Optional.empty());

        // when // then
        assertThatThrownBy(() -> userService.getUser(email))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void 회원삭제() {
        // given
        String email = "donghyoen09@naver.com";

        // when
        userService.deleteUser(email);

        // then
        verify(userRepository).deleteByUsername(email);
    }

    @Test
    void 비밀번호_업데이트() {
        // given
        String username = "donghyheon09@naver.com";
        String password = "123123123";
        User user = User.builder()
                .username(username)
                .password(password)
                .build();
        User returnUser = User.builder()
                .username(username)
                .password("asjfojoasfjoifsa")
                .build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.ofNullable(returnUser));

        // when
        userService.resetPassword(username, password);

        // then
        verify(userRepository).save(user);
    }
}