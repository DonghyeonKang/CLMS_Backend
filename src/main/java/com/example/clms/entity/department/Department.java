package com.example.clms.entity.department;

import com.example.clms.dto.department.DepartmentResponse;
import com.example.clms.entity.university.University;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne(targetEntity = University.class) // university 엔티티와 관계를 설정할 것임을 명시
    @JoinColumn(name = "university_id") // university 엔티티의 id 값으로 칼럼을 join 함. join 한 칼럼명 설정도 할 수 있는데 기본인 university_id 로 사용
    private University university;

    // toDto
    public DepartmentResponse toDto() {
        DepartmentResponse dto = new DepartmentResponse();
        dto.setId(id);
        dto.setName(name);
        return dto;
    }
}
