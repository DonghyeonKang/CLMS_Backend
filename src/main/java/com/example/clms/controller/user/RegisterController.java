package com.example.clms.controller.user;

import com.example.clms.common.ApiResponse;
import com.example.clms.dto.user.RegisterManagerRequest;
import com.example.clms.dto.user.RegisterStudentRequest;
import com.example.clms.service.user.RegisterServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegisterController {
    private final RegisterServiceImpl registerServiceImpl;
    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "학습자회원가입")
    @PostMapping("/student")
    public ApiResponse<?> registerStudent(@RequestBody RegisterStudentRequest model) {
        model.setPassword(passwordEncoder.encode(model.getPassword()));
        registerServiceImpl.register(model.toUserRegisterDto(), "USER");

        return ApiResponse.createSuccessWithNoContent();
    }

    @Operation(summary = "교수자회원가입")
    @PostMapping("/manager")
    public Object registerManager(@RequestBody RegisterManagerRequest model) {
        model.setPassword(passwordEncoder.encode(model.getPassword()));
        registerServiceImpl.register(model.toUserRegisterDto(), "MANAGER");

        return ApiResponse.createSuccessWithNoContent();
    }
}
