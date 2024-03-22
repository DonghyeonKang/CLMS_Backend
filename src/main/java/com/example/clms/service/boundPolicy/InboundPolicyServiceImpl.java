package com.example.clms.service.boundPolicy;

import com.example.clms.common.exception.EmptyDataAccessException;
import com.example.clms.common.exception.ErrorCode;
import com.example.clms.common.shRunner.ShParser;
import com.example.clms.common.shRunner.ShRunner;
import com.example.clms.entity.boundPolicy.InboundPolicy;
import com.example.clms.entity.instance.Instance;
import com.example.clms.entity.server.Server;
import com.example.clms.repository.boundPolicy.InboundPolicyRepository;
import com.example.clms.repository.instance.InstanceRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.clms.dto.boundPolicy.InboundPolicyDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Transactional
@Service
@RequiredArgsConstructor
public class InboundPolicyServiceImpl implements InboundPolicyService{
    private final InboundPolicyRepository inboundPolicyRepository;
    private final InstanceRepository instanceRepository;
    private final EntityManager entityManager;
    private final ShRunner shRunner;
    private final ShParser shParser;

    @Override
    public List<InboundPolicyDto> findAllByInstanceId(int instanceId) {
        List<InboundPolicy> entityList = inboundPolicyRepository.findAllByInstanceId(instanceId);

        List<InboundPolicyDto> dtoList = new ArrayList<>();
        for (InboundPolicy entity : entityList) {
            dtoList.add(entity.toDto());
        }

        return dtoList;
    }

    @Override
    public InboundPolicyDto save(InboundPolicy inboundPolicy) {
        InboundPolicy entity = inboundPolicyRepository.save(inboundPolicy);
        return entity.toDto();
    }

    @Override
    public void saveAll(List<InboundPolicyDto> dtoList) {
        // instance id 가져온 뒤 해당 인스턴스 참조값 받아오기
        int instanceId = dtoList.get(0).getInstanceId();
        Instance userInstance = instanceRepository.getReferenceById(instanceId);

        // 컨트롤러에서 받은 dto 리스트를 반복문으로 entity로 변환. 매번 인자로 instance 엔티티 넘기기
        List<InboundPolicy> newEntityList = new ArrayList<>();
        List<InboundPolicy> updateEntityList = new ArrayList<>();

        for (InboundPolicyDto dto : dtoList) {
            if(dto.getId() == -1) {
                newEntityList.add(dto.toCreatingEntity(userInstance));
            } else {
                updateEntityList.add(dto.toUpdatingEntity(userInstance));
            }
        }

        // 저장 --------------------
        saveAllCreating(newEntityList, instanceId);

        // 업데이트 --------------------
        saveAllUpdating(updateEntityList);
    }

    private void saveAllCreating(List<InboundPolicy> newEntityList, int instanceId) {
        if(newEntityList.size() == 0) {
            return;
        }

        // 생성
        List<InboundPolicy> createdEntityList = inboundPolicyRepository.saveAll(newEntityList);

        // 포트 업데이트
        List<InboundPolicyDto> inboundPolicyDtoList = new ArrayList<>();
        for (InboundPolicy entity : createdEntityList) {
            // hostPort 지정
            entityManager.persist(entity);
            entity.updateHostPort();

            // 응답 리스트에 저장
            inboundPolicyDtoList.add(entity.toDto());
        }

        // 쉘 실행
        Instance entity = instanceRepository.findById(instanceId)
                .orElseThrow(() -> new EmptyDataAccessException(ErrorCode.EMPTY_DATA_ACCESS));
        Server baseServer = entity.getLecture().getServer();
        String containerName = entity.getName() + entity.getId();

        for (InboundPolicyDto inboundPolicyDto: inboundPolicyDtoList) {
            Map result = shRunner.execCommand("AddInbound.sh", baseServer.getServerUsername(),
                    baseServer.getIpv4(), containerName, inboundPolicyDto.getHostPort() + ":" + inboundPolicyDto.getInstancePort(), entity.getStorage().toString(), entity.getOs());

            shParser.isSuccess(result.get(1).toString());
            // TODO: 응답에 따른 예외 생성
        }
    }

    private void saveAllUpdating(List<InboundPolicy> updateEntityList) {
        if(updateEntityList.size() == 0) {
            return;
        }

        for (InboundPolicy entity : updateEntityList) {
            InboundPolicy inboundPolicy = inboundPolicyRepository.findById(entity.getId()).get();

            entityManager.persist(inboundPolicy);
            inboundPolicy.updateAllPort(entity.getHostPort(), entity.getInstancePort());
        }
    }

    @Override
    public void delete(InboundPolicy inboundPolicy) {
        inboundPolicyRepository.delete(inboundPolicy);
    }

    @Override
    public void deleteAll(List<InboundPolicy> inboundPolicyList) {
        List<Long> idList = new ArrayList<>();
        for (InboundPolicy entity : inboundPolicyList) {
            idList.add((long) entity.getId());
        }

        inboundPolicyRepository.deleteAllByIdIn(idList);
    }
}
