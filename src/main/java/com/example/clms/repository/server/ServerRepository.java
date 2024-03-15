package com.example.clms.repository.server;

import com.example.clms.entity.server.Server;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServerRepository {

    List<Server> findAllByDepartmentId(Long departmentId);
    Server getReferenceById(Long id);
    Optional<Long> findServerIdByDepartmentId(Long departmentId);
    Server save(Server server);
    void deleteById(Long serverId);
    Optional<Server> findById(Long serverId);
}

