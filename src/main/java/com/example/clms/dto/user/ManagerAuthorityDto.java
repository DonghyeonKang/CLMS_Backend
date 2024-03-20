package com.example.clms.dto.user;

import com.example.clms.entity.user.ManagerAuthority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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
