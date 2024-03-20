package com.example.clms.service.user;

import com.example.clms.dto.user.ManagerAuthorityDto;
import com.example.clms.dto.user.ManagerAuthorityResponse;

import java.util.List;

public interface ManagerAuthoritiesService {
    void managerVerificationRequest(ManagerAuthorityDto approvalDto, Long userId);
    List<ManagerAuthorityResponse> getManagerVerificationList();
    void approveManagerVerification(String username);
    void denyManagerVerification(String username);
    void deleteManagerVerification(String username);
}