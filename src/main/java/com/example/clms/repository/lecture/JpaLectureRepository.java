package com.example.clms.repository.lecture;

import com.example.clms.entity.lecture.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaLectureRepository extends JpaRepository<Lecture, Long>, LectureRepository {
}
