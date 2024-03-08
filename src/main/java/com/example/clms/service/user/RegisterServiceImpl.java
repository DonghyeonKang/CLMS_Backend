package com.example.clms.service.user;


import com.example.clms.common.exception.ErrorCode;
import com.example.clms.common.exception.RegisterException;
import com.example.clms.dto.user.UserRegisterDto;
import com.example.clms.entity.department.Department;
import com.example.clms.entity.university.University;
import com.example.clms.entity.user.User;
import com.example.clms.repository.department.DepartmentRepository;
import com.example.clms.repository.university.UniversityRepository;
import com.example.clms.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j  // logging framework
@Component  // 컴포넌트 스캔 방식으로 빈 등록
@RequiredArgsConstructor // private field 생성자 주입으로 DI 구현
public class RegisterServiceImpl implements RegisterService {
    final private UserRepository userRepository;
    final private DepartmentRepository departmentRepository;
    final private UniversityRepository universityRepository;

    @Transactional
    public User register(UserRegisterDto userDto, String role) {
        // 회원가입 검증
        validateRegisterRequest(userDto);

        // department, university 객체 가져오기
        Department department = departmentRepository.getReferenceById(userDto.getDepartmentId());
        University university = universityRepository.getReferenceById(userDto.getUniversityId());

        // dto to entity
        User user;
        if(role.equals("USER")) {
            user = userDto.toUserEntity(department, university);
        } else {
            user = userDto.toManagerEntity(department, university);
        }

        // jpa의 save
        return userRepository.save(user);
    }

    private void validateRegisterRequest(UserRegisterDto userDto) {
        // 이메일 중복 검증
        validateDuplicateEmail(userDto.getUsername());
    }

    private void validateDuplicateEmail(String email) {
        if (userRepository.findByUsername(email).isPresent()) {
            throw new RegisterException(ErrorCode.DUPLICATED_EMAIL);
        }
    }
}
