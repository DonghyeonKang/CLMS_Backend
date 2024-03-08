package com.example.clms.dto.mail;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerificationRequest {
    private String authNumber;
    @Email
    private String email;
}
