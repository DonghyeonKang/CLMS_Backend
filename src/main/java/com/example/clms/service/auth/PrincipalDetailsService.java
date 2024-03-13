package com.example.clms.service.auth;

import com.example.clms.common.exception.ErrorCode;
import com.example.clms.common.exception.UserNotFoundException;
import com.example.clms.entity.user.User;
import com.example.clms.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService{

    private final UserRepository userRepository;

    // session 에 User 추가
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {throw new UserNotFoundException(ErrorCode.USER_NOT_FOUND);});
        return new PrincipalDetails(user);
    }
}