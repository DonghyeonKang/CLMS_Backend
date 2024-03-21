package com.example.clms.repository.lecture;

import com.example.clms.entity.lecture.Lecture;

import java.util.List;
import java.util.Optional;

public interface LectureRepository {
    Lecture save(Lecture lecture);
    Lecture getReferenceById(Long id);
    Optional<Lecture> findById(Long lectureId);
    List<Lecture> findAllByDepartmentId(Long departmentId);
    void delete(Lecture lecture);
}
