package com.example.clms.dto.user;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequest {
    private String username;
    private String password;
}
