package com.example.clms.service.user;

import com.example.clms.common.exception.DuplicatedRequestException;
import com.example.clms.common.exception.ErrorCode;
import com.example.clms.common.exception.MemberAuthenticationException;
import com.example.clms.common.exception.UserNotFoundException;
import com.example.clms.dto.user.ManagerAuthorityDto;
import com.example.clms.dto.user.ManagerAuthorityResponse;
import com.example.clms.entity.user.ManagerAuthority;
import com.example.clms.entity.user.User;
import com.example.clms.repository.user.ManagerAuthoritiesRepository;
import com.example.clms.repository.user.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ManagerAuthoritiesServiceImpl implements ManagerAuthoritiesService {
    private final EntityManager entityManager;
    private final ManagerAuthoritiesRepository managerAuthoritiesRepository;
    private final UserRepository userRepository;

    // 관리자 인증 요청하기
    public void managerVerificationRequest(ManagerAuthorityDto approvalDto, Long userId) {
        isManager(userId);
        Optional<ManagerAuthority> managerAuthority = managerAuthoritiesRepository.findByUserId(userId);
        if(managerAuthority.isPresent()) {
            throw new DuplicatedRequestException(ErrorCode.DUPLICATED_REQUEST);
        }

        managerAuthoritiesRepository.save(approvalDto.toManagerAuthority(userId));
    }

    private void isManager(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        if (user.getPhone() == null) {
            throw new MemberAuthenticationException(ErrorCode.NOT_MANAGER);
        }
    }

    // 관리자 인증 요청 리스트 조회
    public List<ManagerAuthorityResponse> getManagerVerificationList() {
        List<ManagerAuthority> managerAuthorityList = managerAuthoritiesRepository.findAll();

        List<ManagerAuthorityResponse> managerAuthorityResponses = new ArrayList<>();
        for(ManagerAuthority managerAuthority : managerAuthorityList) {
            User user = userRepository.findById(managerAuthority.getUserId()).get();

            managerAuthorityResponses.add(ManagerAuthorityResponse.builder()
                            .username(user.getUsername())
                            .university(user.getUniversity().getName())
                            .department(user.getDepartment().getName())
                            .phone(user.getPhone())
                            .status(managerAuthority.getStatus())
                            .build());
        }

        return managerAuthorityResponses;
    }

    // 관리자 인증 요청 승인
    @Transactional
    public void approveManagerVerification(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        ManagerAuthority managerAuthority = managerAuthoritiesRepository.findById(user.getId())
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.EMPTY_DATA_ACCESS));

        entityManager.persist(managerAuthority);
        entityManager.persist(user);
        managerAuthority.acceptAuthorityStatus();
        user.setManager();
    }

    // 관리자 인증 요청 거절
    @Transactional
    public void denyManagerVerification(String username) {

        // username 으로 user 조회
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND)); // 예외처리

        // userId로 approval 조회
        ManagerAuthority managerAuthority = managerAuthoritiesRepository.findById(user.getId())
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND)); // managerAuthority 가 존제하지 않음 에러로 수정해야함

        // 엔티티 영속성 설정
        entityManager.persist(managerAuthority);

        // 조회한 approval accept.
        managerAuthority.denyAuthorityStatus();
    }

    // 관리자 인증 요청 삭제
    public void deleteManagerVerification(String username) {
        // username 으로 user 조회
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND)); // 예외처리

        // userId로 approval 조회
        ManagerAuthority managerAuthority = managerAuthoritiesRepository.findById(user.getId())
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND)); // managerAuthority 가 존제하지 않음 에러로 수정해야함

        // 삭제
        Long verificationId = managerAuthority.getId();
        managerAuthoritiesRepository.deleteById(verificationId);
    }
}
