package com.example.clms.repository.lecture;

import com.example.clms.entity.lecture.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaNoticeRepository extends JpaRepository<Notice, Long>, NoticeRepository {
    List<Notice> findAllByLectureId(Long lectureId);
    Optional<Notice> findById(Long noticeId);
    Notice save(Notice notice);
    void deleteById(Long noticeId);
}
