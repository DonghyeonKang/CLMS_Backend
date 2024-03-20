package com.example.clms.controller.user;

import com.example.clms.common.ApiResponse;
import com.example.clms.dto.user.ManagerAuthorityDto;
import com.example.clms.dto.user.ResetPasswordRequest;
import com.example.clms.service.auth.PrincipalDetails;
import com.example.clms.service.user.ManagerAuthoritiesService;
import com.example.clms.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController // restful api 구성시 필요
@RequiredArgsConstructor    // final 로 선언된 필드 생성자 주입방식으로 DI 하게 해줌. DI 안 해주면 NullPointer exception 나옴
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final ManagerAuthoritiesService managerAuthoritiesService;
    private final PasswordEncoder passwordEncoder;

    // 회원 탈퇴
    @DeleteMapping()
    public void deleteUser(HttpServletRequest req) {
        userService.deleteUser(req.getParameter("username"));
    }

    // 비밀번호 재설정
    @PutMapping("/password")
    public void resetPassword(@RequestBody ResetPasswordRequest model) {
        model.setPassword(passwordEncoder.encode(model.getPassword()));
        userService.resetPassword(model.getUsername(), model.getPassword());
    }

    // 관리자 인증 요청 리스트 조회
    @GetMapping("/manager/verification")
    public ApiResponse<?> getManagerVerificationList() {
        return ApiResponse.createSuccessWithContent(managerAuthoritiesService.getManagerVerificationList());
    }

    // 관리자 인증 요청하기
    @PostMapping("/verification/manager")
    public void managerVerificationRequest(@RequestBody ManagerAuthorityDto approvalDto, Authentication authentication) {
        approvalDto.setStatus("waiting");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        managerAuthoritiesService.managerVerificationRequest(approvalDto, principalDetails.getId());
    }

    // 관리자 인증 요청 승인
    @PostMapping("/manager/verification/accept")
    public void approveManagerVerification(@RequestBody Map<String, String> req) {
        managerAuthoritiesService.approveManagerVerification(req.get("username"));
    }

    // 관리자 인증 요청 거절
    @PostMapping("/manager/verification/deny")
    public void denyManagerVerification(@RequestBody Map<String, String> req) {
        managerAuthoritiesService.denyManagerVerification(req.get("username"));
    }

    // 관리자 인증 요청 삭제
    @PostMapping("/manager/verification/delete")
    public void deleteManagerVerification(@RequestBody Map<String, String> req) {
        managerAuthoritiesService.deleteManagerVerification(req.get("username"));
    }
}
