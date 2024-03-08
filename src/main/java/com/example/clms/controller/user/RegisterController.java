package com.example.clms.controller.user;

import com.example.clms.common.ApiResponse;
import com.example.clms.dto.user.RegisterManagerRequest;
import com.example.clms.dto.user.RegisterStudentRequest;
import com.example.clms.entity.department.Department;
import com.example.clms.service.department.DepartmentService;
import com.example.clms.service.university.UniversityService;
import com.example.clms.service.user.RegisterService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegisterController {
    private final RegisterService registerService;
    private final PasswordEncoder passwordEncoder;
    private final UniversityService universityService;
    private final DepartmentService departmentService;

    @Operation(summary = "모든 학교 조회")
    @GetMapping("/universities")
    public ApiResponse<?> getUniversities() {
        return ApiResponse.createSuccessWithContent(universityService.findAllUniversity());
    }

    @Operation(summary = "모든 학과 조회")
    @GetMapping("/departments")
    public ApiResponse<?> getDepartments(HttpServletRequest req) {
        return ApiResponse.createSuccessWithContent(departmentService.findAllDepartment(Integer.parseInt(req.getParameter("universityId"))));
    }

    @Operation(summary = "학습자회원가입")
    @PostMapping("/student")
    public ApiResponse<?> registerStudent(@RequestBody RegisterStudentRequest model) {
        model.setPassword(passwordEncoder.encode(model.getPassword()));
        registerService.register(model.toUserRegisterDto(), "USER");

        return ApiResponse.createSuccessWithNoContent();
    }

    @Operation(summary = "교수자회원가입")
    @PostMapping("/manager")
    public Object registerManager(@RequestBody RegisterManagerRequest model) {
        model.setPassword(passwordEncoder.encode(model.getPassword()));
        registerService.register(model.toUserRegisterDto(), "MANAGER");

        return ApiResponse.createSuccessWithNoContent();
    }
}
