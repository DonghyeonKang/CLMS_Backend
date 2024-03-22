package com.example.clms.service.domain;

import com.example.clms.dto.domain.DomainDto;

public interface DomainService {
    DomainDto findByInstanceId(Integer instanceId);
    String createDomain(DomainDto domainDto);
    void deleteDomain(DomainDto domainDto);
}
