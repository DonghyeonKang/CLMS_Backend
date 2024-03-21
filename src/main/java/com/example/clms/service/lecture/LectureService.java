package com.example.clms.service.lecture;

import com.example.clms.dto.lecture.ClassRegistrationDto;
import com.example.clms.dto.lecture.CreateLectureRequest;
import com.example.clms.dto.lecture.LectureDto;
import com.example.clms.dto.lecture.StudentDto;

import java.util.List;

public interface LectureService {

    LectureDto createLecture(CreateLectureRequest createLectureRequest);
    List<LectureDto> getLectureList(Long departmentId);
    List<LectureDto> getMyLectureList(Long userId);
    LectureDto getLectureDetail(Long lectureId);
    void deleteLecture(Long lectureId);
    List<StudentDto> getStudentList(Long lectureId);
    List<StudentDto> getStudentListForRegister(Long lectureId);
    void signUpClass(ClassRegistrationDto classRegistrationDto);
    List<StudentDto> approveRegistration(ClassRegistrationDto classRegistrationDto);
    List<StudentDto> refuseRegistration(ClassRegistrationDto classRegistrationDto);
    LectureDto findById(Long lectureId);
}
