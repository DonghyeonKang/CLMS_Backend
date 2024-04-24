package com.example.clms.controller.lecture;

import com.example.clms.common.ApiResponse;
import com.example.clms.dto.lecture.NoticeDto;
import com.example.clms.dto.lecture.PostingNoticeRequest;
import com.example.clms.service.lecture.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lecture/notice")
public class NoticeController {
    private final NoticeService noticeService;

    @GetMapping("/list")
    public ApiResponse<?> getNoticeList(@RequestParam(value = "lectureId") Long lectureId) {
        List<NoticeDto> result = noticeService.getNoticeList(lectureId);
        return ApiResponse.createSuccessWithContent(result);
    }

    @PostMapping()
    public ApiResponse<?> postingNotice(@RequestBody PostingNoticeRequest postingNoticeRequest) {
        NoticeDto result = noticeService.postingNotice(NoticeDto.builder()
                        .title(postingNoticeRequest.getTitle())
                        .content(postingNoticeRequest.getContent())
                        .lectureId(postingNoticeRequest.getLectureId())
                        .createAt(postingNoticeRequest.getCreateAt())
                        .build()
        );

        return ApiResponse.createSuccessWithContent(result);
    }

    @DeleteMapping()
    public void deleteNotice(@RequestParam(value = "noticeId") Long noticeId) {
        noticeService.deleteNotice(noticeId);
    }
}