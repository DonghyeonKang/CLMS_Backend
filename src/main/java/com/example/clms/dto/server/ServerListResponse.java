package com.example.clms.dto.server;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServerListResponse {

    private Long serverId;
    private String name;
    private String ipv4;
    private String hostname;
}
