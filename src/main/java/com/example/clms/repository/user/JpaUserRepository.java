package com.example.clms.repository.user;

import com.example.clms.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<User, Long>, UserRepository {
    Optional<User> findByUsername(String username);
    Optional<List<String>> findUsernameByDepartmentId(int departmentId);
    void deleteByUsername(String username);
}
