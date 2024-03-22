package com.example.clms.entity.boundPolicy;

import com.example.clms.dto.boundPolicy.InboundPolicyDto;
import com.example.clms.entity.instance.Instance;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InboundPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = true)
    private int hostPort;
    private int instancePort;

    @ManyToOne(targetEntity = Instance.class)
    @JoinColumn(name = "instance_id")
    private Instance instance;

    public InboundPolicyDto toDto() {
        return InboundPolicyDto.builder()
                .id(id)
                .hostPort(hostPort)
                .instancePort(instancePort)
                .instanceId(instance.getId())
                .build();
    }

    public void updateHostPort() {
        this.hostPort = 10000 + this.id;
    }

    public void updateAllPort(int hostPort, int instancePort) {
        this.hostPort = hostPort;
        this.instancePort = instancePort;
    }
}
