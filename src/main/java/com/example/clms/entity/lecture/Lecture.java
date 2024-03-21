package com.example.clms.entity.lecture;


import com.example.clms.dto.lecture.LectureDto;
import com.example.clms.entity.department.Department;
import com.example.clms.entity.server.Server;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String lectureName;
    private String introducing;
    @ManyToOne(targetEntity = Server.class)
    @JoinColumn(name = "server_id")
    private Server server;

    @ManyToOne(targetEntity = Department.class)
    @JoinColumn(name = "department_id")
    private Department department;

    public LectureDto toDto() {
        return LectureDto.builder()
                .lectureName(lectureName)
                .introducing(introducing)
                .serverId(server.getId())
                .id(id)
                .build();
    }
}
