package com.example.clms.repository.lecture;

import com.example.clms.entity.lecture.LectureUser;

import java.util.List;
import java.util.Optional;

public interface LectureUserRepository {
    Optional<LectureUser> findById(Long id);
    LectureUser save(LectureUser lectureUser);
    List<LectureUser> findAllByWaitingUserId(Long lectureId);
    List<LectureUser> findAllByPermittedUserId(Long lectureId);
    List<LectureUser> findAllByUserId(Long userId);
}