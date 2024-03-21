package com.example.clms.repository.lecture;

import com.example.clms.entity.lecture.LectureUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaLectureUserRepository extends JpaRepository<LectureUser, Long>, LectureUserRepository {
    @Query(value = "select * from lecture_user where lecture_id=?1 and permit='waiting'", nativeQuery = true)
    List<LectureUser> findAllByWaitingUserId(Long lectureId);

    @Query(value = "select * from lecture_user where lecture_id=?1 and permit='permit'", nativeQuery = true)
    List<LectureUser> findAllByPermittedUserId(Long lectureId);

    @Query(value = "select * from lecture_user where user_id=?1 and permit='permit'", nativeQuery = true)
    List<LectureUser> findAllByUserId(Long userId);
}
