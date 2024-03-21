package com.example.clms.service.lecture;


import com.example.clms.dto.lecture.NoticeDto;

import java.util.List;

public interface NoticeService {
    List<NoticeDto> getNoticeList(Long lectureId);
    NoticeDto postingNotice(NoticeDto noticeDto);
    void deleteNotice(Long noticeId);
}
