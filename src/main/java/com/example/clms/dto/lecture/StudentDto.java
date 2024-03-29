package com.example.clms.dto.lecture;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {
    private String name;
    private int studentId;
    private Long id;
}
