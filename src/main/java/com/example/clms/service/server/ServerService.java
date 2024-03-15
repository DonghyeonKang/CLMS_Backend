package com.example.clms.service.server;

import com.example.clms.dto.server.ServerDto;
import com.example.clms.dto.server.ServerListResponse;
import com.example.clms.dto.server.ServerResourceResponse;

import java.util.List;

public interface ServerService {
    ServerDto registerServer(ServerDto serverDto);
    List<ServerListResponse> getServerList(Long departmentId);
    ServerResourceResponse getServerResource(Long serverId);
    void deleteServer(Long serverId);
    ServerDto findById(Long serverId);
}
