package com.example.clms.controller.lecture;

import com.example.clms.common.ApiResponse;
import com.example.clms.dto.lecture.*;
import com.example.clms.service.auth.PrincipalDetails;
import com.example.clms.service.lecture.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lecture")
public class LectureController {
    private final LectureService lectureService;

    @GetMapping("/my")
    public ApiResponse<?> getMyLectureList(Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        List<LectureDto> result = lectureService.getMyLectureList(principalDetails.getUser().getId());
        return ApiResponse.createSuccessWithContent(result);
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
    public ApiResponse<?> getStudentList(@RequestParam(value = "id") Long lectureId) {
        List<StudentDto> result = lectureService.getStudentList(lectureId);
        return ApiResponse.createSuccessWithContent(result);
    }

    // 수강 신청한 학생 목록
    @GetMapping("/student/register")
    public ApiResponse<?> getStudentListForRegister(@RequestParam(value = "id") Long lectureId) {
        List<StudentDto> result = lectureService.getStudentListForRegister(lectureId);
        return ApiResponse.createSuccessWithContent(result);
    }

    // 수강 신청
    @PostMapping("/student")
    public void signUpClass(@RequestBody Map<String, Long> param, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        lectureService.signUpClass(SignUpClassDto.builder()
                        .lectureId(param.get("lectureId"))
                        .userId(principalDetails.getUser().getId())
                        .build());
    }

    // 수강 신청 승인
    @PostMapping("/student/registration")
    public ApiResponse<?> approveRegistration(@RequestBody ApproveRegistrationRequest approveRegistrationRequest) {
        List<StudentDto> result = lectureService.approveRegistration(approveRegistrationRequest);
        return ApiResponse.createSuccessWithContent(result);
    }

    // 수강 신청 거절
    @PostMapping("/student/refusal")
    public ApiResponse<?> refuseRegistration(@RequestBody RefuseRegistrationRequest refuseRegistrationRequest) {
        List<StudentDto> result = lectureService.refuseRegistration(refuseRegistrationRequest);
        return ApiResponse.createSuccessWithContent(result);
    }
}
