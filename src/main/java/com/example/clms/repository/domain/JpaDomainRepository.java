package com.example.clms.repository.domain;

import com.example.clms.entity.domain.Domain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaDomainRepository extends JpaRepository<Domain, Integer>, DomainRepository {
    Optional<Domain> findByInstanceId(Integer instanceId);
    Domain save(Domain domain);
    void delete(Domain domain);
}
