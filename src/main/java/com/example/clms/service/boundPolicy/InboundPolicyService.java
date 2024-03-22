package com.example.clms.service.boundPolicy;

import com.example.clms.dto.boundPolicy.InboundPolicyDto;
import com.example.clms.entity.boundPolicy.InboundPolicy;

import java.util.List;

public interface InboundPolicyService {

    List<InboundPolicyDto> findAllByInstanceId(int instanceId);

    InboundPolicyDto save(InboundPolicy inboundPolicy);

    void saveAll(List<InboundPolicyDto> inboundPolicyList);

    void delete(InboundPolicy inboundPolicy);

    void deleteAll(List<InboundPolicy> inboundPolicyList);
}
