package com.example.clms.dto.user;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterManagerRequest {
    private String username;
    private String password;
    private String phone;
    private String universityId;
    private String departmentId;
    private String name;

    public UserRegisterDto toUserRegisterDto() {
        return UserRegisterDto.builder()
                .username(username)
                .password(password)
                .phone(phone)
                .departmentId(Long.parseLong(departmentId))
                .universityId(Integer.parseInt(universityId))
                .name(name)
                .build();
    }
}
