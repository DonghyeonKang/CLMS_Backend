package com.example.clms.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterManagerRequest {
    @Email
    @NotNull
    @NotEmpty
    private String username;
    @NotNull
    @NotEmpty
    private String password;
    @NotNull
    @NotEmpty
    private String phone;
    @NotNull
    @NotEmpty
    private String universityId;
    @NotNull
    @NotEmpty
    private String departmentId;
    @NotNull
    @NotEmpty
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
