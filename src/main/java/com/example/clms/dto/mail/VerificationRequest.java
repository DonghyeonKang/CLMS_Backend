package com.example.clms.dto.mail;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerificationRequest {
    @NotNull
    @NotEmpty
    private String authNumber;
    @Email
    @NotNull
    @NotEmpty
    private String email;
}
