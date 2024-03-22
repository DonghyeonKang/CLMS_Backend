package com.example.clms.service.instance;

import com.example.clms.common.shRunner.ParserResponseDto;
import com.example.clms.dto.instance.InstanceDto;

import java.util.List;

public interface InstanceService {
    int findMyInstanceId(Long userId, Long lectureId);
    void createInstance(InstanceDto instanceDto, String username);
    InstanceDto findByInstanceId(int instanceId);
    List<InstanceDto> findAllByLectureId(Long lectureId);
    void startInstance(int instanceId, String username);
    void stopInstance(int instanceId, String username);
    void restartInstance(int instanceId, String username);
    void deleteInstance(int instanceId, String username);
    void createKeyPair(String hostName, String keyPairName);
    ParserResponseDto checkContainerResource(String hostName, String hostIp, String containerName);
    ParserResponseDto checkServerResource(String hostName, String hostIp);
}
