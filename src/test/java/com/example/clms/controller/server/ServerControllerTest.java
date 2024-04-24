package com.example.clms.controller.server;

import com.example.clms.dto.server.ServerDto;
import com.example.clms.dto.server.ServerListResponse;
import com.example.clms.dto.server.ServerRegisterRequest;
import com.example.clms.dto.server.ServerResourceResponse;
import com.example.clms.service.server.ServerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ServerController.class)
class ServerControllerTest {

    @Autowired
    ObjectMapper mapper;
    @Autowired
    MockMvc mvc;
    @MockBean
    private ServerService serverService;

    private static final String BASE_URL = "http://localhost:8080";

    @Test
    @WithMockUser // 스프링 시큐리티 통과
    void 서버등록() throws Exception {
        // given
        ServerRegisterRequest serverRegisterRequest = ServerRegisterRequest.builder()
                .ipv4("123.123.1.12")
                .serverName("웹 프로그래밍 서버")
                .serverUsername("user")
                .departmentId("1")
                .build();
        String body = mapper.writeValueAsString(
                serverRegisterRequest
        );

        ServerDto serverDto = ServerDto.builder()
                .ipv4("123.123.1.12")
                .serverName("웹 프로그래밍 서버")
                .serverUsername("user")
                .departmentId(1L)
                .build();

        when(serverService.registerServer(any(ServerDto.class)))
                .thenReturn(serverDto);

        // when // then
        mvc.perform(post(BASE_URL + "/servers/register/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.ipv4").value("123.123.1.12"))
                .andExpect(jsonPath("$.data.serverName").value("웹 프로그래밍 서버"))
                .andExpect(jsonPath("$.data.serverUsername").value("user"))
                .andExpect(jsonPath("$.data.departmentId").value(1));
    }

    @Test
    @WithMockUser
    void 서버리스트조회() throws Exception {
        // given
        List<ServerListResponse> serverList = new ArrayList<>();
        ServerListResponse serverListResponse1 = ServerListResponse.builder()
                .serverId(1L)
                .name("웹 프로그래밍 서버")
                .ipv4("123.123.1.12")
                .hostname("user")
                .build();
        serverList.add(serverListResponse1);

        ServerDto serverDto = ServerDto.builder()
                .ipv4("123.123.1.12")
                .serverName("웹 프로그래밍 서버")
                .serverUsername("user")
                .departmentId(1L)
                .build();

        when(serverService.getServerList(1L))
                .thenReturn(serverList);

        // when // then
        mvc.perform(get(BASE_URL + "/servers/management/list")
                        .param("departmentId", "1")
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].ipv4").value("123.123.1.12"))
                .andExpect(jsonPath("$.data[0].name").value("웹 프로그래밍 서버"))
                .andExpect(jsonPath("$.data[0].hostname").value("user"))
                .andExpect(jsonPath("$.data[0].serverId").value(1));
    }

    @Test
    @WithMockUser
    void 서버리소스조회() throws Exception {
        // given
        List<JSONObject> jsonObjectList = new ArrayList<>();
        Map<String, String> data = new HashMap<>();
        data.put("memory", "1GB");
        JSONObject jsonObject1 = new JSONObject(data);
        jsonObjectList.add(jsonObject1);

        ServerResourceResponse serverResourceResponse = ServerResourceResponse.builder()
                .resultList(jsonObjectList)
                .success(true)
                .build();

        when(serverService.getServerResource(1L))
                .thenReturn(serverResourceResponse);

        // when // then
        mvc.perform(get(BASE_URL + "/servers/management/resources")
                        .param("serverId", "1")
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.success").value(true))
                .andExpect(jsonPath("$.data.resultList[0].memory").value("1GB"));
    }

    @Test
    @WithMockUser
    void 서버삭제() throws Exception {
        // given

        // when // then
        mvc.perform(delete(BASE_URL + "/servers/")
                        .param("serverId", "1")
                        .with(csrf())
                )
                .andExpect(status().isOk());

        verify(serverService).deleteServer(1L);
    }

    @Test
    @WithMockUser
    void clms패키지다운로드_fileNotExist() throws Exception {
        // given
        File file = new File("");
        when(serverService.getFile())
                .thenReturn(file);

        // when // then
        mvc.perform(get(BASE_URL + "/servers/register/clmsPackage.tar"))
                .andExpect(status().is5xxServerError());
    }

    @Test
    @WithMockUser
    void clms패키지다운로드() throws Exception {
        // given
        File file = new File("/Users/donghyeonkang/project/CLMS_Backend/src/main/resources/clmsPackage.tar");
        when(serverService.getFile())
                .thenReturn(file);

        // when // then
        mvc.perform(get(BASE_URL + "/servers/register/clmsPackage.tar"))
                .andExpect(status().isOk());
    }

}