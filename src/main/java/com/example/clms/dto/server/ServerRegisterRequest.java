package com.example.clms.dto.server;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServerRegisterRequest {
    private String ipv4;
    private String serverName;
    private String serverUsername;
    private String departmentId;

    public ServerDto toServerDto() {
        return ServerDto.builder()
                .ipv4(ipv4)
                .serverName(serverName)
                .serverUsername(serverUsername)
                .departmentId(Long.parseLong(departmentId))
                .build();
    }
}
