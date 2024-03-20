package com.example.clms.repository.user;

import com.example.clms.entity.user.ManagerAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaManagerAuthoritiesRepository extends JpaRepository<ManagerAuthority, Long>, ManagerAuthoritiesRepository {
}
