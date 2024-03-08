package com.example.clms.service.user;

import com.example.clms.dto.user.UserRegisterDto;
import com.example.clms.entity.user.User;

public interface RegisterService {
    User register(UserRegisterDto userDto, String role);
}
