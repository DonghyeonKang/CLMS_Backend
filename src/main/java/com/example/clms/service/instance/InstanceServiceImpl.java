package com.example.clms.service.instance;

import com.example.clms.common.exception.EmptyDataAccessException;
import com.example.clms.common.exception.ErrorCode;
import com.example.clms.common.shRunner.ParserResponseDto;
import com.example.clms.common.shRunner.ShParser;
import com.example.clms.common.shRunner.ShRunner;
import com.example.clms.dto.instance.InstanceDto;
import com.example.clms.entity.boundPolicy.InboundPolicy;
import com.example.clms.entity.instance.Instance;
import com.example.clms.entity.lecture.Lecture;
import com.example.clms.entity.server.Server;
import com.example.clms.entity.user.User;
import com.example.clms.repository.boundPolicy.InboundPolicyRepository;
import com.example.clms.repository.instance.InstanceRepository;
import com.example.clms.repository.lecture.LectureRepository;
import com.example.clms.repository.user.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class InstanceServiceImpl implements InstanceService{

    private final UserRepository userRepository;
    private final LectureRepository lectureRepository;
    private final InstanceRepository instanceRepository;
    private final InboundPolicyRepository inboundPolicyRepository;
    private final EntityManager entityManager;
    private final ShRunner shRunner;
    private final ShParser shParser;

    @Override
    public int findMyInstanceId(Long userId, Long lectureId) {
        return instanceRepository.findIdByUserIdAndLectureId(userId, lectureId)
                .orElseThrow(() -> new EmptyDataAccessException(ErrorCode.EMPTY_DATA_ACCESS));
    }

    @Transactional
    @Override
    public void createInstance(InstanceDto instanceDto, String username) {
        User newUser = userRepository.getReferenceById(instanceDto.getUserId());
        Lecture lecture = lectureRepository.findById(instanceDto.getLectureId())
                .orElseThrow(() -> new EmptyDataAccessException(ErrorCode.EMPTY_DATA_ACCESS));
        Server baseServer = lecture.getServer();
        instanceDto.setAddress(baseServer.getIpv4());

        // 인스턴스 저장, 쉘 스크립트 실행, 인바운드 정책 저장, 공개키 전송
        username = username.replaceAll("[@.]", "");
        Instance instance = instanceRepository.save(instanceDto.toEntity(newUser, lecture));
        createInstanceByShell(instance, baseServer, username);
        createInboundPolicy(instance);
        sendPublicKeyByShell(instance, baseServer, username);
    }

    private void createInstanceByShell(Instance instance, Server baseServer, String username) {
        // ssh port 값 생성
        int instanceId = instance.getId();
        int port = 2000 + instanceId;
        instance.updateInstancePort(port);

        try {
            Map result = shRunner.execCommand("CreateContainer.sh", baseServer.getServerUsername(), baseServer.getIpv4(),
                    Integer.toString(instance.getPort()), "22",
                    username, Integer.toString(instance.getId()),
                    Double.toString(instance.getStorage()), instance.getOs());

            if (!shParser.isSuccess(result.get(1).toString())) {
                instanceRepository.deleteById(instance.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void createInboundPolicy(Instance instance) {
        // 인바운드 추가
        InboundPolicy inboundPolicy = InboundPolicy.builder()
                .instance(instance)
                .instancePort(22)
                .hostPort(instance.getPort())
                .build();
        inboundPolicyRepository.save(inboundPolicy);
    }
    private void sendPublicKeyByShell(Instance instance, Server baseServer, String username) {
        try {
            Map result = shRunner.execCommand("SendPublickey.sh", baseServer.getServerUsername(), baseServer.getIpv4(),
                    username + Integer.toString(instance.getId()),
                    instance.getKeyName());

            instanceRepository.deleteById(instance.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 인스턴스 조회
    @Override
    public InstanceDto findByInstanceId(int instanceId) {
        return instanceRepository.findById(instanceId)
                .orElseThrow(() -> new EmptyDataAccessException(ErrorCode.EMPTY_DATA_ACCESS))
                .toDto();
    }

    // serverId 로 전체 리스트 가져오기.
    @Override
    public List<InstanceDto> findAllByLectureId(Long lectureId) {
        List<Instance> instanceList = instanceRepository.findAllByLectureId(lectureId);
        List<InstanceDto> instanceDtoList = new ArrayList<>();
        for (Instance instance : instanceList) {
            instanceDtoList.add(instance.toDto());
        }
        return instanceDtoList;
    }

    // 정지된 인스턴스 실행
    @Transactional
    @Override
    public void startInstance(int instanceId, String username) {
        Instance instance = instanceRepository.findById(instanceId).get();
        Server baseServer = instance.getLecture().getServer();

        Map result = shRunner.execCommand("StartContainer.sh", baseServer.getServerUsername(),
                baseServer.getIpv4(), username + instance.getId());
        // TODO: 응답 결과에 따른 예외 생성

        entityManager.persist(instance);
        instance.updateStatus("running");
    }

    // 인스턴스 정지
    @Transactional
    @Override
    public void stopInstance(int instanceId, String username) {
        Instance instance = instanceRepository.findById(instanceId)
                .orElseThrow(() -> new EmptyDataAccessException(ErrorCode.EMPTY_DATA_ACCESS));
        Server baseServer = instance.getLecture().getServer();

        Map result = shRunner.execCommand("StopContainer.sh", baseServer.getServerUsername(),
                baseServer.getIpv4(), username + instance.getId());
        // TODO: 응답 결과에 따른 예외 생성

        entityManager.persist(instance);
        instance.updateStatus("stopped");
    }

    // 인스턴스 재시작
    @Transactional
    @Override
    public void restartInstance(int instanceId, String username) {
        Instance instance = instanceRepository.findById(instanceId)
                .orElseThrow(() -> new EmptyDataAccessException(ErrorCode.EMPTY_DATA_ACCESS));
        Server baseServer = instance.getLecture().getServer();

        Map result = shRunner.execCommand("RestartContainer.sh", baseServer.getServerUsername(),
                baseServer.getIpv4(), username + instance.getId());
        // TODO: 응답 결과에 따른 예외 생성

        entityManager.persist(instance);
        instance.updateStatus("running");
    }

    // 인스턴스 삭제
    @Transactional
    @Override
    public void deleteInstance(int instanceId, String username) {
        Instance instance = instanceRepository.findById(instanceId)
                .orElseThrow(() -> new EmptyDataAccessException(ErrorCode.EMPTY_DATA_ACCESS));
        Server baseServer = instance.getLecture().getServer();

        Map result = shRunner.execCommand("RemoveContainer.sh", baseServer.getServerUsername(),
                baseServer.getIpv4(), username + instance.getId());
        // TODO: 쉘 결과에 따른 예외 생성

        instanceRepository.deleteById(instanceId);
    }

    // CLMS 서버에 키 생성 ~/keys/서버_계정명/사용자_입력_키_이름.pem, pub
    @Override
    public void createKeyPair(String hostName, String keyName) {
        Map result = shRunner.execCommand("CreateKeypairs.sh", hostName, keyName);
        // TODO: 쉘 결과에 따른 예외 생성
    }

    // 컨테이너 리소스 조회
    @Override
    public ParserResponseDto checkContainerResource(String hostName, String hostIp, String containerName) {
        Map result = shRunner.execCommand("CheckContainerResource.sh", hostName, hostIp, containerName);
        // TODO: 쉘 결과에 따른 예외 생성
        return shParser.checkContainerResource(result.get(1).toString());
    }

    // 서버 리소스 조회
    @Override
    public ParserResponseDto checkServerResource(String hostName, String hostIp) {
        Map result = shRunner.execCommand("CheckServerResource.sh", hostName, hostIp);
        // TODO: 쉘 결과에 따른 예외 생성
        return shParser.checkServerResource(result.get(1).toString());
    }
}
