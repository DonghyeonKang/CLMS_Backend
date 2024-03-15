package com.example.clms.entity.server;

import com.example.clms.entity.department.Department;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Server {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String ipv4;
    private String serverUsername;
    @ManyToOne(targetEntity = Department.class)
    @JoinColumn(name = "department_id")
    private Department department;
}