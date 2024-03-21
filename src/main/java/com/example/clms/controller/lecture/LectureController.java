package com.example.clms.controller.lecture;

import com.example.clms.common.ApiResponse;
import com.example.clms.dto.lecture.ClassRegistrationDto;
import com.example.clms.dto.lecture.CreateLectureRequest;
import com.example.clms.dto.lecture.LectureDto;
import com.example.clms.dto.lecture.StudentDto;
import com.example.clms.service.auth.PrincipalDetails;
import com.example.clms.service.lecture.LectureService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lecture")
public class LectureController {
    private final LectureService lectureService;

    @GetMapping("/my")
    public JSONObject getMyLectureList(Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        List<LectureDto> result = lectureService.getMyLectureList(principalDetails.getUser().getId());
        JSONObject obj = new JSONObject();
        obj.put("lectureList", result);
        return obj;
    }

    @GetMapping
    public ApiResponse<?> getLectureList(@RequestParam(value = "departmentId") Long departmentId) {
        List<LectureDto> result = lectureService.getLectureList(departmentId);
        return ApiResponse.createSuccessWithContent(result);
    }

    @GetMapping("/detail")
    public ApiResponse<?> getLectureDetail(@RequestParam(value = "id") Long lectureId) {
        LectureDto result = lectureService.getLectureDetail(lectureId);
        return ApiResponse.createSuccessWithContent(result);
    }

    @PostMapping
    public ApiResponse<?> createLecture(@RequestBody CreateLectureRequest createLectureRequest) {
        LectureDto lectureDto = lectureService.createLecture(createLectureRequest);
        return ApiResponse.createSuccessWithContent(lectureDto);
    }

    @DeleteMapping
    public void deleteLecture(@RequestParam(value = "id") Long lectureId) {
        lectureService.deleteLecture(lectureId);
    }

    // 수강 신청된 학생 목록
    @GetMapping("/student")
    public JSONObject getStudentList(@RequestParam(value = "id") Long lectureId) {
        List<StudentDto> result = lectureService.getStudentList(lectureId);

        JSONObject obj = new JSONObject();
        obj.put("studentList", result);
        return obj;
    }

    // 수강 신청한 학생 목록
    @GetMapping("/student/register")
    public JSONObject getStudentListForRegister(@RequestParam(value = "id") Long lectureId) {
        List<StudentDto> result = lectureService.getStudentListForRegister(lectureId);

        JSONObject obj = new JSONObject();
        obj.put("studentList", result);
        return obj;
    }

    // 수강 신청
    @PostMapping("/student")
    public void signUpClass(@RequestBody ClassRegistrationDto classRegistrationDto , Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        classRegistrationDto.setUserId(principalDetails.getUser().getId());
        lectureService.signUpClass(classRegistrationDto);
    }

    // 수강 신청 승인
    @PostMapping("/student/registration")
    public JSONObject approveRegistration(@RequestBody ClassRegistrationDto classRegistrationDto, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        classRegistrationDto.setUserId(principalDetails.getUser().getId());
        List<StudentDto> result = lectureService.approveRegistration(classRegistrationDto);

        JSONObject obj = new JSONObject();
        obj.put("studentList", result);
        return obj;
    }

    // 수강 신청 거절
    @PostMapping("/student/refusal")
    public JSONObject refuseRegistration(@RequestBody ClassRegistrationDto classRegistrationDto, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        classRegistrationDto.setUserId(principalDetails.getUser().getId());
        List<StudentDto> result = lectureService.refuseRegistration(classRegistrationDto);

        JSONObject obj = new JSONObject();
        obj.put("studentList", result);
        return obj;
    }
}
