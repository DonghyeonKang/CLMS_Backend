package com.example.clms.service.user;

import com.example.clms.common.exception.DuplicatedRequestException;
import com.example.clms.common.exception.MemberAuthenticationException;
import com.example.clms.common.exception.UserNotFoundException;
import com.example.clms.dto.user.ManagerAuthorityDto;
import com.example.clms.dto.user.ManagerAuthorityResponse;
import com.example.clms.entity.department.Department;
import com.example.clms.entity.university.University;
import com.example.clms.entity.user.ManagerAuthority;
import com.example.clms.entity.user.Roles;
import com.example.clms.entity.user.User;
import com.example.clms.repository.user.ManagerAuthoritiesRepository;
import com.example.clms.repository.user.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class ManagerAuthoritiesServiceImplTest {
    private ManagerAuthoritiesService managerAuthoritiesService;

    @Mock
    private EntityManager entityManager;
    @Mock
    private ManagerAuthoritiesRepository managerAuthoritiesRepository;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        managerAuthoritiesService = new ManagerAuthoritiesServiceImpl(entityManager, managerAuthoritiesRepository, userRepository);
    }

    @Test
    void 관리자인증_요청하기() {
        // given
        ManagerAuthorityDto approvalDto = ManagerAuthorityDto.builder()
                .username("donghyeon09@naver.com")
                .status("waiting")
                .build();
        Long userId = Long.parseLong("1");
        User user = User.builder()
                .id(Long.parseLong("1"))
                .username("donghyeon09@naver.com")
                .phone("123-1234-1234")
                .roles(Roles.USER)
                .build();
        ManagerAuthority managerAuthority = approvalDto.toManagerAuthority(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(user));
        when(managerAuthoritiesRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // when
        managerAuthoritiesService.managerVerificationRequest(approvalDto, userId);

        // then
        verify(managerAuthoritiesRepository).save(refEq(managerAuthority));
    }

    @Test
    void 관리자인증_요청하기_USER_NOT_FOUND_예외() {
        // given
        ManagerAuthorityDto approvalDto = ManagerAuthorityDto.builder()
                .username("donghyeon09@naver.com")
                .status("waiting")
                .build();
        Long userId = Long.parseLong("1");
        User user = User.builder()
                .id(Long.parseLong("1"))
                .username("donghyeon09@naver.com")
                .phone("123-1234-1234")
                .roles(Roles.USER)
                .build();
        ManagerAuthority managerAuthority = approvalDto.toManagerAuthority(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        when(managerAuthoritiesRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // when // then
        assertThatThrownBy(() -> managerAuthoritiesService.managerVerificationRequest(approvalDto, userId))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void 관리자인증_요청하기_NOT_관리자_예외() {
        // given
        ManagerAuthorityDto approvalDto = ManagerAuthorityDto.builder()
                .username("donghyeon09@naver.com")
                .status("waiting")
                .build();
        Long userId = Long.parseLong("1");
        User user = User.builder()
                .id(Long.parseLong("1"))
                .username("donghyeon09@naver.com")
                .roles(Roles.USER)
                .build();
        ManagerAuthority managerAuthority = approvalDto.toManagerAuthority(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(user));
        when(managerAuthoritiesRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // when // then
        assertThatThrownBy(() -> managerAuthoritiesService.managerVerificationRequest(approvalDto, userId))
                .isInstanceOf(MemberAuthenticationException.class);
    }

    @Test
    void 관리자인증_요청하기_중복요청() {
        // given
        ManagerAuthorityDto approvalDto = ManagerAuthorityDto.builder()
                .username("donghyeon09@naver.com")
                .status("waiting")
                .build();
        Long userId = Long.parseLong("1");
        User user = User.builder()
                .id(Long.parseLong("1"))
                .username("donghyeon09@naver.com")
                .phone("123-1234-1234")
                .roles(Roles.USER)
                .build();
        ManagerAuthority managerAuthority = approvalDto.toManagerAuthority(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(user));
        when(managerAuthoritiesRepository.findByUserId(userId)).thenReturn(Optional.ofNullable(managerAuthority));

        // when // then
        assertThatThrownBy( () -> managerAuthoritiesService.managerVerificationRequest(approvalDto, userId))
                .isInstanceOf(DuplicatedRequestException.class);

    }

    @Test
    void 관리자인증_요청리스트_조회() {
        // given
        ManagerAuthority managerAuthority1 = ManagerAuthority.builder()
                .id(1L)
                .userId(1L)
                .status("waiting")
                .build();

        List<ManagerAuthority> managerAuthorityList = new ArrayList<>();
        managerAuthorityList.add(managerAuthority1);

        University university = University.builder()
                .id(1)
                .name("경상국립대학교")
                .build();
        Department department = Department.builder()
                .id(1L)
                .name("컴퓨터과학과")
                .university(university)
                .build();


        User user = User.builder()
                .id(1L)
                .phone("123-123-123")
                .username("donghyeon09@naver.com")
                .university(university)
                .department(department)
                .build();

        List<ManagerAuthorityResponse> managerAuthorityResponses = new ArrayList<>();
        managerAuthorityResponses.add(ManagerAuthorityResponse.builder()
                        .username("donghyeon09@naver.com")
                        .university(university.getName())
                        .department(department.getName())
                        .phone("123-123-123")
                        .status("waiting")
                .build());

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        when(managerAuthoritiesRepository.findAll()).thenReturn(managerAuthorityList);

        // when
        List<ManagerAuthorityResponse> returnManagerAuthorityResponses = managerAuthoritiesService.getManagerVerificationList();

        // then
        Assertions.assertEquals(returnManagerAuthorityResponses, managerAuthorityResponses);
    }

    @Test
    void 관리자인증_요청_승인() {
        // given
        String username = "donghyeon09@naver.com";
        User user = User.builder()
                .id(1L)
                .username("donghyeon09@naver.com")
                .build();
        when(userRepository.findByUsername(username)).thenReturn(Optional.ofNullable(user));

        ManagerAuthority managerAuthority = ManagerAuthority.builder()
                .userId(1L)
                .status("waiting")
                .build();
        when(managerAuthoritiesRepository.findById(Objects.requireNonNull(user).getId())).thenReturn(Optional.ofNullable(managerAuthority));

        // when
        managerAuthoritiesService.approveManagerVerification(username);

        // then
        assertThat(Objects.requireNonNull(managerAuthority).getStatus()).isEqualTo("accepted");
    }

    @Test
    void 관리자인증_요청_승인_존재하지않는사용자_예외() {
        // given
        String username = "donghyeon09@naver.com";
        User user = User.builder()
                .id(1L)
                .username("donghyeon09@naver.com")
                .build();
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        ManagerAuthority managerAuthority = ManagerAuthority.builder()
                .userId(1L)
                .status("waiting")
                .build();
        when(managerAuthoritiesRepository.findById(Objects.requireNonNull(user).getId())).thenReturn(Optional.ofNullable(managerAuthority));

        // when // then
        assertThatThrownBy(() -> managerAuthoritiesService.approveManagerVerification(username))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void 관리자인증_요청_승인_존재하지않는요청_예외() {
        // given
        String username = "donghyeon09@naver.com";
        User user = User.builder()
                .id(1L)
                .username("donghyeon09@naver.com")
                .build();
        when(userRepository.findByUsername(username)).thenReturn(Optional.ofNullable(user));

        ManagerAuthority managerAuthority = ManagerAuthority.builder()
                .userId(1L)
                .status("waiting")
                .build();
        when(managerAuthoritiesRepository.findById(Objects.requireNonNull(user).getId())).thenReturn(Optional.empty());

        // when // then
        assertThatThrownBy(() -> managerAuthoritiesService.approveManagerVerification(username))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void 관리자인증_요청_거절() {
        // given
        String username = "donghyeon09@naver.com";
        User user = User.builder()
                .id(1L)
                .username("donghyeon09@naver.com")
                .build();
        when(userRepository.findByUsername(username)).thenReturn(Optional.ofNullable(user));

        ManagerAuthority managerAuthority = ManagerAuthority.builder()
                .userId(1L)
                .status("waiting")
                .build();
        when(managerAuthoritiesRepository.findById(Objects.requireNonNull(user).getId())).thenReturn(Optional.ofNullable(managerAuthority));

        // when
        managerAuthoritiesService.denyManagerVerification(username);

        // then
        assertThat(Objects.requireNonNull(managerAuthority).getStatus()).isEqualTo("denied");
    }

    @Test
    void 관리자인증_요청_거절_존재하지않는사용자_예외() {
        // given
        String username = "donghyeon09@naver.com";
        User user = User.builder()
                .id(1L)
                .username("donghyeon09@naver.com")
                .build();
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        ManagerAuthority managerAuthority = ManagerAuthority.builder()
                .userId(1L)
                .status("waiting")
                .build();
        when(managerAuthoritiesRepository.findById(Objects.requireNonNull(user).getId())).thenReturn(Optional.ofNullable(managerAuthority));

        // when // then
        assertThatThrownBy(() -> managerAuthoritiesService.denyManagerVerification(username))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void 관리자인증_요청_거절_존재하지않는요청_예외() {
        // given
        String username = "donghyeon09@naver.com";
        User user = User.builder()
                .id(1L)
                .username("donghyeon09@naver.com")
                .build();
        when(userRepository.findByUsername(username)).thenReturn(Optional.ofNullable(user));

        ManagerAuthority managerAuthority = ManagerAuthority.builder()
                .userId(1L)
                .status("waiting")
                .build();
        when(managerAuthoritiesRepository.findById(Objects.requireNonNull(user).getId())).thenReturn(Optional.empty());

        // when // then
        assertThatThrownBy(() -> managerAuthoritiesService.denyManagerVerification(username))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void 관리자인증_요청_삭제() {
        // given
        String username = "donghyeon09@naver.com";
        User user = User.builder()
                .id(1L)
                .username("donghyeon09@naver.com")
                .build();
        when(userRepository.findByUsername(username)).thenReturn(Optional.ofNullable(user));

        ManagerAuthority managerAuthority = ManagerAuthority.builder()
                .id(1L)
                .userId(1L)
                .status("waiting")
                .build();
        when(managerAuthoritiesRepository.findById(Objects.requireNonNull(user).getId())).thenReturn(Optional.ofNullable(managerAuthority));

        // when
        managerAuthoritiesService.deleteManagerVerification(username);

        // then
        verify(managerAuthoritiesRepository).deleteById(Objects.requireNonNull(managerAuthority).getId());
    }

    @Test
    void 관리자인증_요청_삭제_존재하지않는사용자_예외() {
        // given
        String username = "donghyeon09@naver.com";
        User user = User.builder()
                .id(1L)
                .username("donghyeon09@naver.com")
                .build();
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        ManagerAuthority managerAuthority = ManagerAuthority.builder()
                .userId(1L)
                .status("waiting")
                .build();
        when(managerAuthoritiesRepository.findById(Objects.requireNonNull(user).getId())).thenReturn(Optional.ofNullable(managerAuthority));

        // when // then
        assertThatThrownBy(() -> managerAuthoritiesService.deleteManagerVerification(username))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void 관리자인증_요청_삭제_존재하지않는요청_예외() {
        // given
        String username = "donghyeon09@naver.com";
        User user = User.builder()
                .id(1L)
                .username("donghyeon09@naver.com")
                .build();
        when(userRepository.findByUsername(username)).thenReturn(Optional.ofNullable(user));

        ManagerAuthority managerAuthority = ManagerAuthority.builder()
                .userId(1L)
                .status("waiting")
                .build();
        when(managerAuthoritiesRepository.findById(Objects.requireNonNull(user).getId())).thenReturn(Optional.empty());

        // when // then
        assertThatThrownBy(() -> managerAuthoritiesService.deleteManagerVerification(username))
                .isInstanceOf(UserNotFoundException.class);
    }
}