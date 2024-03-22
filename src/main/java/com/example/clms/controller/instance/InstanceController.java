package com.example.clms.controller.instance;

import com.example.clms.common.ApiResponse;
import com.example.clms.common.shRunner.ParserResponseDto;
import com.example.clms.common.shRunner.ShRunner;
import com.example.clms.dto.boundPolicy.InboundPolicyDto;
import com.example.clms.dto.domain.DomainDto;
import com.example.clms.dto.domain.DomainInstanceRequest;
import com.example.clms.dto.instance.ControlInstanceRequest;
import com.example.clms.dto.instance.CreateInstanceRequest;
import com.example.clms.dto.instance.InstanceDto;
import com.example.clms.dto.instance.InstanceListResponse;
import com.example.clms.dto.lecture.LectureDto;
import com.example.clms.dto.server.ServerDto;
import com.example.clms.service.auth.PrincipalDetails;
import com.example.clms.service.boundPolicy.InboundPolicyService;
import com.example.clms.service.domain.DomainService;
import com.example.clms.service.instance.InstanceService;
import com.example.clms.service.lecture.LectureService;
import com.example.clms.service.server.ServerService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/instances")
public class InstanceController {
    private final InstanceService instanceService;
    private final DomainService domainService;
    private final InboundPolicyService inboundPolicyService;
    private final ServerService serverService;
    private final LectureService lectureService;
    private final ShRunner shRunner;

    // 인스턴스 생성 후 인스턴스 목록으로 이동. 실패(오류 발생) 시 생성 페이지로 돌아가기.
    @PostMapping("/creation")
    public ApiResponse<?> createInstance(@RequestBody CreateInstanceRequest request, Authentication authentication) {
        // 로그인된 사용자의 userId 추가
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Timestamp curTimestamp = Timestamp.valueOf(LocalDateTime.now()); // 현재 시간 저장(LocalDateTime을 mySQL에서 호환되도록 Timestamp로 형변환)

        InstanceDto newDto = InstanceDto.builder()
                .lectureId(request.getLectureId())
                .userId(principalDetails.getId())
                .created(curTimestamp)
                .os(request.getOs())
                .name(request.getName())
                .state("running")
                .code(1)
                .keyName(request.getKeyName())
                .storage(Double.parseDouble(request.getStorage().substring(0, request.getStorage().length() - 1)))
                .address(request.getAddress())
                .build();

        // 요청에 따라 쉘 스크립트 실행
        instanceService.createInstance(newDto, principalDetails.getUsername());
        return ApiResponse.createSuccessWithNoContent();
    }

    // 키페어 생성
    @PostMapping("/keypair")
    public ResponseEntity<Resource> createKeypair(@RequestBody Map<String, String> testName) {
        String hostName = testName.get("hostname");
        String keyName = testName.get("name");

        instanceService.createKeyPair(hostName, keyName);
        String fileName = keyName + ".pem";
        Resource resource = new FileSystemResource("/home/ubuntu/Keys/" + hostName + "/" + fileName);
        String mimeType = "application/x-pem-file";

        // 파일 다운로드를 위한 HttpHeaders 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        headers.setContentType(MediaType.parseMediaType(mimeType));

        // 파일을 Response Entity 에 포함하여 반환
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

    @GetMapping("/detail")
    public ApiResponse<?> instanceDetail(@RequestParam Integer instanceId) {
        InstanceDto result =  instanceService.findByInstanceId(instanceId);
        return ApiResponse.createSuccessWithContent(result);
    }

    // 인스턴스 시작
    @PostMapping("/start")
    public ApiResponse<?> startInstance(@RequestBody ControlInstanceRequest request, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        instanceService.startInstance(request.getInstanceId(), principalDetails.getUsername());

        return ApiResponse.createSuccessWithNoContent();
    }

    // 인스턴스 재시작
    @PostMapping("/restart")
    public ApiResponse<?> restartInstance(@RequestBody ControlInstanceRequest request, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        instanceService.restartInstance(request.getInstanceId(), principalDetails.getUsername());

        // 쉘 스크립트 실행
        shRunner.execCommand("H_RestartContainer.sh");

        return ApiResponse.createSuccessWithNoContent();
    }

    // 인스턴스 정지
    @PostMapping("/stop")
    public ApiResponse<?> stopInstance(@RequestBody ControlInstanceRequest request, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        instanceService.stopInstance(request.getInstanceId(), principalDetails.getUsername());

        // 쉘 스크립트 실행
        shRunner.execCommand("H_StopContainer.sh");

        return ApiResponse.createSuccessWithNoContent();
    }

    // 인스턴스 삭제
    @PostMapping("/delete")
    public ApiResponse<?> deleteInstance(@RequestBody ControlInstanceRequest request, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        instanceService.deleteInstance(request.getInstanceId(), principalDetails.getUsername());

        // 쉘 스크립트 실행
        shRunner.execCommand("H_StopContainer.sh");

        return ApiResponse.createSuccessWithNoContent();
    }

    // 강의 내 인스턴스 목록 조회
    @GetMapping("/list")
    public ApiResponse<?> listByLectureId(@RequestParam(value = "lectureId", required = false) Integer lectureId) {
        List<InstanceListResponse> result = new ArrayList<>(); // 반환할 리스트

        List<InstanceDto> list = instanceService.findAllByLectureId(lectureId.longValue());
        for (InstanceDto dto : list) {      // 조회 페이지에 띄울 내용만 새 dto 리스트에 담기
            InstanceListResponse newDto = new InstanceListResponse();
            newDto.setInstanceId(dto.getInstanceId());
            newDto.setName(dto.getName());
            newDto.setUserName("dong");
            result.add(newDto);
        }
        return ApiResponse.createSuccessWithContent(result);
    }

    // 특정 인스턴스의 도메인 조회(instanceId)
    @GetMapping("/domain")
    public ApiResponse<?> getDomainName(@RequestParam Integer instanceId) {
        DomainDto result = domainService.findByInstanceId(instanceId);
        return ApiResponse.createSuccessWithContent(result);
    }

    // 특정 인스턴스의 도메인 저장(추가)
    @PostMapping("/domain")
    public ApiResponse<?> domainCreate(@RequestBody DomainInstanceRequest domainInstanceRequest) {
        String domainName = domainService.createDomain(
                new DomainDto(domainInstanceRequest.getDomainName(), domainInstanceRequest.getInstanceId()));

        Map<String, String> result = new HashMap<>();
        return ApiResponse.createSuccessWithContent(result.put("domainName", domainName));
    }

    // 특정 인스턴스의 도메인 삭제
    @PostMapping("/domain/remove")
    public ApiResponse<?> domainDelete(@RequestBody(required = false) DomainInstanceRequest domainInstanceRequest) {
        System.out.println(domainInstanceRequest.getInstanceId());
        domainService.deleteDomain(
                DomainDto.builder()
                        .instanceId(domainInstanceRequest.getInstanceId())
                        .build());

        return ApiResponse.createSuccessWithNoContent();
    }

    // 특정 인스턴스의 인바운드 리스트 조회
    @GetMapping("/inbounds/list")
    public ApiResponse<?> inboundList(@RequestParam(required = false) Integer instanceId) {
        List<InboundPolicyDto> result = inboundPolicyService.findAllByInstanceId(instanceId);

        return ApiResponse.createSuccessWithContent(result);
    }

    @PutMapping("/inbounds/setting")
    public ApiResponse<?> inboundSetting(@RequestBody List<InboundPolicyDto> inbounds) {
        inboundPolicyService.saveAll(inbounds);

        return ApiResponse.createSuccessWithNoContent();
    }


    // 자원 사용량 조회 - 컨테이너(학생)
    @GetMapping("/resource/container")
    public ApiResponse<?> checkContainerResource(Authentication authentication, @RequestParam Integer instanceId) {
        InstanceDto instanceDto = instanceService.findByInstanceId(instanceId);
        LectureDto lectureDto = lectureService.findById(instanceId.longValue());
        ServerDto serverDto = serverService.findById(lectureDto.getServerId());
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        ParserResponseDto result = instanceService.checkContainerResource(serverDto.getServerUsername(), serverDto.getIpv4(),
                principalDetails.getUsername() + instanceDto.getInstanceId());

        return ApiResponse.createSuccessWithContent(result);
    }

    // 자원 사용량 조회 - 서버(관리자)
    @GetMapping("/resource/server")
    public ApiResponse<?> checkServerResource(@RequestParam Long serverId) {
        ServerDto serverDto = serverService.findById(serverId);
        ParserResponseDto result = instanceService.checkServerResource(serverDto.getServerUsername(),
                serverDto.getIpv4());

        return ApiResponse.createSuccessWithContent(result);
    }

    // 강의 내 본인 인스턴스 id 조회
    @GetMapping("/id")
    public ApiResponse<?> getMyInstanceId(@RequestParam Long lectureId, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        Map<String, Integer> result = new HashMap<>();
        result.put("instanceId", instanceService.findMyInstanceId(principalDetails.getId(), lectureId));
        return ApiResponse.createSuccessWithContent(result);
    }
}