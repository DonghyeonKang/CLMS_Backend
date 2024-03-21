package com.example.clms.service.lecture;

import com.example.clms.dto.lecture.*;

import java.util.List;

public interface LectureService {

    LectureDto createLecture(CreateLectureRequest createLectureRequest);
    List<LectureDto> getLectureList(Long departmentId);
    List<LectureDto> getMyLectureList(Long userId);
    LectureDto getLectureDetail(Long lectureId);
    void deleteLecture(Long lectureId);
    List<StudentDto> getStudentList(Long lectureId);
    List<StudentDto> getStudentListForRegister(Long lectureId);
    void signUpClass(SignUpClassDto signUpClassDto);
    List<StudentDto> approveRegistration(ApproveRegistrationRequest approveRegistrationRequest);
    List<StudentDto> refuseRegistration(RefuseRegistrationRequest refuseRegistrationRequest);
    LectureDto findById(Long lectureId);
}
