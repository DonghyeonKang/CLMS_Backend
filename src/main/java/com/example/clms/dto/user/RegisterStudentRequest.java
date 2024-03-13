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
public class RegisterStudentRequest {
    @Email
    @NotNull
    @NotEmpty
    private String username;
    @NotNull
    @NotEmpty
    private String password;
    @NotNull
    @NotEmpty
    private String universityId;
    @NotNull
    @NotEmpty
    private String departmentId;
    @NotNull
    private int no;
    @NotNull
    @NotEmpty
    private String name;

    public UserRegisterDto toUserRegisterDto() {
        return UserRegisterDto.builder()
                .username(username)
                .name(name)
                .no(no)
                .password(password)
                .departmentId(Long.parseLong(departmentId))
                .universityId(Integer.parseInt(universityId))
                .build();
    }
}
