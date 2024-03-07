package com.example.clms.entity.university;

import com.example.clms.dto.university.UniversityDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class University {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    public UniversityDto toDto() {
        UniversityDto dto = new UniversityDto();
        dto.setId(id);
        dto.setName(name);
        return dto;
    }
}
