package com.example.clms.repository.domain;

import com.example.clms.entity.domain.Domain;

import java.util.Optional;

public interface DomainRepository {
    Optional<Domain> findByInstanceId(Integer instanceId);
    Domain save(Domain domain);
    void deleteByInstanceId(Integer instanceId);
}