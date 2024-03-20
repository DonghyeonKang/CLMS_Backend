package com.example.clms.dto.server;

import com.example.clms.entity.department.Department;
import com.example.clms.entity.server.Server;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServerDto {
    private String ipv4;
    private String serverName;
    private String serverUsername;
    private Long departmentId;
    public Server toEntity(Department department) {
        return Server.builder()
                .name(serverName)
                .ipv4(ipv4)
                .serverUsername(serverUsername)
                .department(department)
                .build();
    }
}
