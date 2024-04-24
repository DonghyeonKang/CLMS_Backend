package com.example.clms.controller.lecture;

import com.example.clms.dto.lecture.NoticeDto;
import com.example.clms.dto.lecture.PostingNoticeRequest;
import com.example.clms.service.lecture.NoticeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@WebMvcTest(NoticeController.class)
class NoticeControllerTest {

    @Autowired
    ObjectMapper mapper;
    @Autowired
    MockMvc mvc;
    @MockBean
    NoticeService noticeService;
    private static final String BASE_URL = "http://localhost:8080";

    @Test
    @WithMockUser
    void 공지사항리스트조회() throws Exception {
        // given
        NoticeDto noticeDto1 = NoticeDto.builder()
                .noticeId(1L)
                .title("공지사항1")
                .content("공지 내용")
                .lectureId(1L)
                .createAt("2018-04-23")
                .build();
        NoticeDto noticeDto2 = NoticeDto.builder()
                .noticeId(1L)
                .title("공지사항2")
                .content("공지 내용")
                .lectureId(1L)
                .createAt("2018-04-23")
                .build();
        List<NoticeDto> noticeDtoList = new ArrayList<>();
        noticeDtoList.add(noticeDto1);
        noticeDtoList.add(noticeDto2);

        when(noticeService.getNoticeList(1L)).thenReturn(noticeDtoList);

        // when // then
        mvc.perform(get(BASE_URL + "/lecture/notice/list")
                        .param("lectureId", "1")
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].title").value("공지사항1"))
                .andExpect(jsonPath("$.data[1].title").value("공지사항2"));
    }

    @Test
    @WithMockUser
    void 공지사항등록() throws Exception {
        // given
        PostingNoticeRequest postingNoticeRequest = PostingNoticeRequest.builder()
                .title("공지사항")
                .content("내용")
                .createAt("2018-01-23")
                .lectureId(1L)
                .build();
        String body = mapper.writeValueAsString(postingNoticeRequest);

        // when // then
        mvc.perform(post(BASE_URL + "/lecture/notice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"));
    }

    @Test
    @WithMockUser
    void 공지사항삭제() throws Exception {
        // given
        // when // then
        mvc.perform(delete(BASE_URL + "/lecture/notice")
                .param("noticeId","1")
                .with(csrf())
        );
        verify(noticeService).deleteNotice(1L);
    }
}