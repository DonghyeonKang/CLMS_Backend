package com.example.clms.repository.boundPolicy;


import com.example.clms.entity.boundPolicy.InboundPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InboundPolicyRepository extends JpaRepository<InboundPolicy, Integer> {
    List<InboundPolicy> findAllByInstanceId(Integer instanceId);
    void deleteAllByIdIn(List<Long> idList);
}
