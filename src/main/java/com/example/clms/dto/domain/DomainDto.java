package com.example.clms.dto.domain;

import com.example.clms.entity.domain.Domain;
import com.example.clms.entity.instance.Instance;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DomainDto {
    private String name;
    private Integer instanceId;

    public Domain toEntity(Instance instance) {
        return Domain.builder()
                .name(name)
                .instance(instance)
                .build();
    }
}
