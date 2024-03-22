package com.example.clms.service.domain;

import com.example.clms.common.exception.EmptyDataAccessException;
import com.example.clms.common.exception.ErrorCode;
import com.example.clms.common.shRunner.ShRunner;
import com.example.clms.dto.domain.DomainDto;
import com.example.clms.entity.domain.Domain;
import com.example.clms.entity.instance.Instance;
import com.example.clms.entity.server.Server;
import com.example.clms.repository.domain.DomainRepository;
import com.example.clms.repository.instance.InstanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Component
@RequiredArgsConstructor    // private field 를 생성자 주입 방식으로 DI
public class DomainServiceImpl implements DomainService {
    private final DomainRepository domainRepository;
    private final InstanceRepository instanceRepository;
    private final ShRunner shRunner;
    public DomainDto findByInstanceId(Integer instanceId) {
        Domain domain = domainRepository.findByInstanceId(instanceId)
                .orElseThrow(() -> new EmptyDataAccessException(ErrorCode.EMPTY_DATA_ACCESS));

        return domain.toDto();
    }

    public String createDomain(DomainDto domainDto) {
        Instance entity = instanceRepository.findById(domainDto.getInstanceId()).get();
        Server baseServer = entity.getLecture().getServer();

        Map result = shRunner.execCommand("H_DeleteNginx.sh", baseServer.getServerUsername(),
                baseServer.getIpv4(), domainDto.getName(), "80");
        // TODO: 응답 결과에 따른 예외 생성

        return domainRepository.save(domainDto.toEntity(entity)).getName();
    }

    @Transactional
    public void deleteDomain(DomainDto domainDto) {
        Instance entity = instanceRepository.findById(domainDto.getInstanceId()).get();
        Server baseServer = entity.getLecture().getServer();

        Map result = shRunner.execCommand("H_AddNginx.sh", baseServer.getServerUsername(),
                baseServer.getIpv4(), domainDto.getName(), "80");
        // TODO: 응답 결과에 따른 예외 생성

        domainRepository.deleteByInstanceId(domainDto.getInstanceId());
    }
}
