package com.example.clms.repository.user;

import com.example.clms.entity.user.ManagerAuthority;

import java.util.List;
import java.util.Optional;

public interface ManagerAuthoritiesRepository {
    ManagerAuthority save(ManagerAuthority managerAuthority);
    List<ManagerAuthority> findAll();
    Optional<ManagerAuthority> findById(Long id);
    void deleteById(Long id);
    Optional<ManagerAuthority> findByUserId(Long userId);
}
