package com.example.clms.dto.user;

import com.example.clms.entity.department.Department;
import com.example.clms.entity.university.University;
import com.example.clms.entity.user.Roles;
import com.example.clms.entity.user.User;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor  // 매개변수가 없는 생성자
@AllArgsConstructor // 모든 필드를 매개변수로 받는 생성자
public class UserRegisterDto {

    private int id;
    private String username;
    private String name;
    private String password;
    private String role;
    private int no;
    private String phone;
    private int universityId;
    private Long departmentId;

    public User toUserEntity(Department department, University university, Roles role) {
        return User.builder()
                .username(username)
                .password(password)
                .roles(role)
                .department(department)
                .no(no)
                .name(name)
                .university(university)
                .build();
    }

    public User toManagerEntity(Department department, University university, Roles role) {
        return User.builder()
                .username(username)
                .password(password)
                .roles(role)
                .phone(phone)
                .department(department)
                .university(university)
                .name(name)
                .build();
    }
}
