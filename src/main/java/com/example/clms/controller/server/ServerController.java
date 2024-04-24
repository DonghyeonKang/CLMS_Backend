package com.example.clms.controller.server;

import com.example.clms.common.ApiResponse;
import com.example.clms.dto.server.ServerDto;
import com.example.clms.dto.server.ServerListResponse;
import com.example.clms.dto.server.ServerRegisterRequest;
import com.example.clms.dto.server.ServerResourceResponse;
import com.example.clms.service.server.ServerService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/servers")
public class ServerController {
    private final ServerService serverService;

    // 서버 등록
    @PostMapping("/register/new")
    public ApiResponse<?> registerServer(@RequestBody ServerRegisterRequest serverRegisterRequest) {
        ServerDto result = serverService.registerServer(serverRegisterRequest.toServerDto());
        return ApiResponse.createSuccessWithContent(result);
    }

    // 서버 등록 자동화 파일 다운로드
    // wget http://clms.kro.kr/servers/register/clmsPackage.tar 로 다운로드 할 수 있도록 함
    @GetMapping("/register/clmsPackage.tar")
    public void getServerizeFile(HttpServletResponse response) {
        File file = serverService.getFile();

        response.setContentType("application/x-tar");
        response.setHeader("Content-Disposition", "attachment; filename=\"clmsPackage.tar\"");
        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            ServletOutputStream so = response.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(so);

            int data;
            while ((data = bis.read()) != -1) {
                bos.write(data);
            }

            bos.close();
            fis.close();
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }

    // 서버 리스트 조회
    @GetMapping("/management/list")
    public ApiResponse<?> getServerList(@RequestParam(value = "departmentId") Long departmentId) {
        List<ServerListResponse> serverList =  serverService.getServerList(departmentId);
        return ApiResponse.createSuccessWithContent(serverList);
    }

    // 서버 리소스 조회
    @GetMapping("/management/resources")
    public ApiResponse<?> getServerResource(HttpServletRequest req) {
        ServerResourceResponse serverResourceResponse = serverService.getServerResource(Long.parseLong(req.getParameter("serverId")));
        return ApiResponse.createSuccessWithContent(serverResourceResponse);
    }

    // 서버 삭제
    @DeleteMapping("/")
    public void deleteServer(HttpServletRequest req) {
        serverService.deleteServer(Long.parseLong(req.getParameter("serverId"))); // serverId 파라미터로 받아옴
    }
}