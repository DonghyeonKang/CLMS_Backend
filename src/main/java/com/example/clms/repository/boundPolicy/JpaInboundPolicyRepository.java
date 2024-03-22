package com.example.clms.repository.boundPolicy;

import com.example.clms.entity.boundPolicy.InboundPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaInboundPolicyRepository extends JpaRepository<InboundPolicy, Long>{
}
