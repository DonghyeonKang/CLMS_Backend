package com.example.clms.entity.domain;

import com.example.clms.dto.domain.DomainDto;
import com.example.clms.entity.instance.Instance;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Builder
@Getter
@NoArgsConstructor // builder 를 사용하기 위해 추가
@AllArgsConstructor // builder 를 사용하기 위해 추가
public class Domain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @OneToOne(targetEntity = Instance.class)  // 단방향 관계 형성은 한 쪽에서만 관계를 넣어주면 된다.
    @JoinColumn(name = "instance_id")   // instance 엔티티의 id 칼럼 값을 매핑한다는 의미
    private Instance instance;

    public DomainDto toDto() {
        return DomainDto.builder()
                .name(name)
                .instanceId(instance.getId())
                .build();
    }
}

