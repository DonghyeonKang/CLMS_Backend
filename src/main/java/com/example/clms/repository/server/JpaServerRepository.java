package com.example.clms.repository.server;

import com.example.clms.entity.server.Server;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaServerRepository extends JpaRepository<Server, Long>, ServerRepository {
}
