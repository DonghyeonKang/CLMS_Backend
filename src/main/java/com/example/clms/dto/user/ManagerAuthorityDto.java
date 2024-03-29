package com.example.clms.dto.user;

import com.example.clms.entity.user.ManagerAuthority;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagerAuthorityDto {
    private String username;
    private String status;

    public ManagerAuthority toManagerAuthority(Long userId) {
        return ManagerAuthority.builder()
                .userId(userId)
                .status(status)
                .build();
    }
}
