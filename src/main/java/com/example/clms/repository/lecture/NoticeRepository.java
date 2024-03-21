package com.example.clms.repository.lecture;


import com.example.clms.entity.lecture.Notice;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository {
    List<Notice> findAllByLectureId(Long lectureId);
    Optional<Notice> findById(Long noticeId);
    Notice save(Notice notice);
    void deleteById(Long noticeId);
}
