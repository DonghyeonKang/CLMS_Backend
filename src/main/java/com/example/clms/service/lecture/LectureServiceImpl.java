package com.example.clms.service.lecture;

import com.example.clms.common.exception.DuplicatedRequestException;
import com.example.clms.common.exception.EmptyDataAccessException;
import com.example.clms.common.exception.ErrorCode;
import com.example.clms.common.exception.UserNotFoundException;
import com.example.clms.dto.lecture.ClassRegistrationDto;
import com.example.clms.dto.lecture.CreateLectureRequest;
import com.example.clms.dto.lecture.LectureDto;
import com.example.clms.dto.lecture.StudentDto;
import com.example.clms.entity.department.Department;
import com.example.clms.entity.lecture.Lecture;
import com.example.clms.entity.lecture.LectureUser;
import com.example.clms.entity.server.Server;
import com.example.clms.repository.department.DepartmentRepository;
import com.example.clms.repository.lecture.LectureRepository;
import com.example.clms.repository.lecture.LectureUserRepository;
import com.example.clms.repository.server.ServerRepository;
import com.example.clms.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LectureServiceImpl implements LectureService {
    private final UserRepository userRepository;
    private final LectureRepository lectureRepository;
    private final ServerRepository serverRepository;
    private final LectureUserRepository lectureUserRepository;
    private final DepartmentRepository departmentRepository;

    // 강의 생성
    @Override
    public LectureDto createLecture(CreateLectureRequest createLectureRequest) {
        Server server = serverRepository.getReferenceById(createLectureRequest.getServerId());
        Department department = departmentRepository.getReferenceById(createLectureRequest.getDepartmentId());

        Lecture lecture = Lecture.builder()
                .lectureName(createLectureRequest.getLectureName())
                .introducing(createLectureRequest.getIntroducing())
                .server(server)
                .department(department)
                .build();
        Lecture newLecture = lectureRepository.save(lecture);
        return newLecture.toDto();
    }

    // 전체 강의 목록
    @Override
    public List<LectureDto> getLectureList(Long departmentId) {
        List<Lecture> lectureList = lectureRepository.findAllByDepartmentId(departmentId);

        return lectureList.stream()
                .map(Lecture::toDto)
                .collect(Collectors.toList());
    }

    // 내 강의 목록
    public List<LectureDto> getMyLectureList(Long userId) {
        List<LectureUser> lectureUserList = lectureUserRepository.findAllByUserId(userId);
        List<LectureDto> result = new ArrayList<>();

        for(LectureUser lectureUser : lectureUserList) {
            LectureDto newDto = new LectureDto();
            Lecture lecture = lectureRepository.findById(lectureUser.getLecture().getId())
                    .orElseThrow(() -> new EmptyDataAccessException(ErrorCode.EMPTY_DATA_ACCESS));

            newDto.setId(lecture.getId());
            newDto.setLectureName(lecture.getLectureName());
            newDto.setNoticeCount(0);
            newDto.setIntroducing(lecture.getIntroducing());
            result.add(newDto);
        }
        return result;
    }

    // 강의 상세
    @Override
    public LectureDto getLectureDetail(Long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new EmptyDataAccessException(ErrorCode.EMPTY_DATA_ACCESS));

        return lecture.toDto();
    }

    // 강의 삭제
    @Override
    public void deleteLecture(Long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new EmptyDataAccessException(ErrorCode.EMPTY_DATA_ACCESS));
        lectureRepository.delete(lecture);
    }

    // 수강 신청된 학생 목록
    @Override
    public List<StudentDto> getStudentList(Long lectureId) {

        List<LectureUser> lectureUsers = lectureUserRepository.findAllByPermittedUserId(lectureId);

        List<StudentDto> result = new ArrayList<>();
//
//        for (LectureUser lectureUser : lectureUsers) {
//            StudentDto newDto = new StudentDto();
//            newDto.setId((lectureUser.getId()));
//            newDto.setStudentId(lectureUser.getUser().getNo());
//            newDto.setName(lectureUser.getUser().getName());
//            result.add(newDto);
//        }

        return result;
    }

    // 수강 신청한 학생 목록
    @Override
    public List<StudentDto> getStudentListForRegister(Long lectureId) {
        List<LectureUser> lectureUsers = lectureUserRepository.findAllByWaitingUserId(lectureId);

        List<StudentDto> result = new ArrayList<>();
//
//        for (LectureUser lectureUser : lectureUsers) {
//            StudentDto newDto = new StudentDto();
//            newDto.setStudentId(lectureUser.getUser().getNo());
//            newDto.setName(lectureUser.getUser().getName());
//            newDto.setId((lectureUser.getId()));
//            result.add(newDto);
//        }

        return result;
    }

    // 수강 신청
    @Transactional
    public void signUpClass(ClassRegistrationDto classRegistrationDto) {
//        User user = userRepository.getReferenceById(classRegistrationDto.getUserId());
//        Lecture lecture = lectureRepository.getReferenceById(classRegistrationDto.getLectureId());
//        System.out.println("---------------------");
//        System.out.println(classRegistrationDto.getLectureId());
//        // 엔티티 생성
//        LectureUser newLectureUser = LectureUser.builder()
//                .user(user)
//                .lecture(lecture)
//                .build();
//
//        // 엔티티 저장
//        LectureUser lectureUser = lectureUserRepository.save(newLectureUser);
    }

    // 수강 신청 승인
    @Transactional
    public List<StudentDto> approveRegistration(ClassRegistrationDto classRegistrationDto) {
        // 엔티티 탐색
        LectureUser lectureUser = lectureUserRepository.findById(classRegistrationDto.getId())
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        // 수정
        lectureUser.setPermit();
        lectureUserRepository.save(lectureUser);

        List<StudentDto> result = getStudentListForRegister(lectureUser.getLecture().getId());
        return result;
    }


    // 수강 신청 거절
   @Transactional
    public List<StudentDto> refuseRegistration(ClassRegistrationDto classRegistrationDto) {
        // 엔티티 탐색
        LectureUser lectureUser = lectureUserRepository.findById(classRegistrationDto.getId())
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        // 수정
        lectureUser.setRefuse();
        lectureUserRepository.save(lectureUser);

       List<StudentDto> result = getStudentListForRegister(lectureUser.getLecture().getId());
       return result;
   }

    public LectureDto findById(Long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        return lecture.toDto();
    }
}
