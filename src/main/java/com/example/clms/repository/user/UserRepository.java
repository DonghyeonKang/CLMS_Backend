package com.example.clms.repository.user;

import com.example.clms.entity.user.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    Optional<List<String>> findUsernameByDepartmentId(int departmentId);
    void deleteByUsername(String username);
}
