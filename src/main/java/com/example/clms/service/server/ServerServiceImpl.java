package com.example.clms.service.server;

import com.example.clms.common.exception.EmptyDataAccessException;
import com.example.clms.common.exception.ErrorCode;
import com.example.clms.common.shRunner.ParserResponseDto;
import com.example.clms.common.shRunner.ShParser;
import com.example.clms.common.shRunner.ShRunner;
import com.example.clms.dto.server.ServerDto;
import com.example.clms.dto.server.ServerListResponse;
import com.example.clms.dto.server.ServerResourceResponse;
import com.example.clms.entity.department.Department;
import com.example.clms.entity.server.Server;
import com.example.clms.repository.department.DepartmentRepository;
import com.example.clms.repository.server.ServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ServerServiceImpl implements ServerService {
    private final ServerRepository serverRepository;
    private final DepartmentRepository departmentRepository;
    private final ShRunner shRunner;
    private final ShParser shParser;

    // 서버 등록
    public ServerDto registerServer(ServerDto serverDto) {
        Department department = departmentRepository.getReferenceById(serverDto.getDepartmentId());
        Server newServer = serverRepository.save(serverDto.toEntity(department));

        return ServerDto.builder()
                .serverUsername(newServer.getServerUsername())
                .ipv4(newServer.getIpv4())
                .departmentId(newServer.getDepartment().getId())
                .serverName(newServer.getName())
                .build();
    }

    // 학과 서버 목록 조회
    public List<ServerListResponse> getServerList(Long departmentId) {
        List<Server> servers = serverRepository.findAllByDepartmentId(departmentId);
        List<ServerListResponse> serverList = new ArrayList<>();

        for(Server server : servers) {
            serverList.add(
                    ServerListResponse.builder()
                            .serverId(server.getId())
                            .name(server.getName())
                            .ipv4(server.getIpv4())
                            .hostname(server.getServerUsername())
                            .build()
            );
        }

        return serverList;
    }

    // 서버의 리소스 (램, 디스크 사용량, 서버 연결 상태) 조회 -> .sh 실행 후 리턴 값 편집
    public ServerResourceResponse getServerResource(Long serverId) {
        Server baseServer = serverRepository.findById(serverId).get();
        ServerResourceResponse serverResourceResponse = new ServerResourceResponse();
        // serverId 로 쉘스크립트에 필요한 파라미터 DB에서 가져와 만들어야함

        // 쉘 실행
        try {
            // 서버 리소스 확인
            Map result = shRunner.execCommand("CheckServerResource.sh", baseServer.getServerUsername(),
                    baseServer.getIpv4());

            // 쉘 리턴 값 파싱
            ParserResponseDto parserResponseDto = shParser.checkServerResource(result.get(1).toString());

            if(shParser.isSuccess(result.get(1).toString())) {
                return parserResponseDto.toDto();
            }

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 서버 삭제
    public void deleteServer(Long serverId) {
        try {
            serverRepository.deleteById(serverId);
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyDataAccessException(ErrorCode.EMPTY_DATA_ACCESS);
        }
    }

    // 서버의 연결 상태 확인 -> .sh 실행 후 리턴 값 편집
    private String getServerConnection(Long serverId) {
        Server baseServer = serverRepository.findById(serverId).get();

        // 쉘 실행
        try {
            // 인스턴스 제거
            Map result = shRunner.execCommand("IsConnected.sh", baseServer.getServerUsername(),
                    baseServer.getIpv4());

            if(shParser.isSuccess(result.get(1).toString())) {
                return "connected";
            }

            return "disconnected";
        } catch (Exception e) {
            return e.toString();
        }
    }

    public ServerDto findById(Long serverId) {
        Server server = serverRepository.findById(serverId).get();

        ServerDto dto = new ServerDto();
        dto.setServerName(server.getName());
        dto.setServerUsername(server.getServerUsername());
        dto.setIpv4(server.getIpv4());
        dto.setDepartmentId(server.getDepartment().getId());

        return dto;
    }

    public File getFile() {
        return new File("/Users/donghyeonkang/project/CLMS_Backend/src/main/resources/clmsPackage.tar");
    }
}
