package com.example.clms.service.user;

import com.example.clms.dto.user.UserRegisterDto;

public interface RegisterService {
    void register(UserRegisterDto userDto, String role);
}
