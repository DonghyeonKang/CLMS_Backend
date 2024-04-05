package com.example.clms.service.department;

import com.example.clms.dto.department.DepartmentResponse;
import com.example.clms.entity.department.Department;
import com.example.clms.entity.university.University;
import com.example.clms.repository.department.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;
    private DepartmentService departmentService;

    @BeforeEach
    void setUp() {
        departmentService = new DepartmentServiceImpl(departmentRepository);
    }

    @Test
    void 모든학과조회() {
        // given
        List<Department> departmentList = new ArrayList<>();
        University university = University.builder()
                .id(1)
                .name("경상국립대학교")
                .build();
        Department department1 = Department.builder()
                .id(1L)
                .university(university)
                .name("컴퓨터과학과")
                .build();
        Department department2 = Department.builder()
                .id(1L)
                .university(university)
                .name("정보통계학과")
                .build();
        departmentList.add(department1);
        departmentList.add(department2);

        when(departmentRepository.findAllByUniversity_Id(1)).thenReturn(departmentList);

        List<DepartmentResponse> departmentResponseList = new ArrayList<>();
        departmentResponseList.add(department1.toDto());
        departmentResponseList.add(department2.toDto());

        // when
        List<DepartmentResponse> returnDepartmentResponseList = departmentService.findAllDepartment(1);

        // then
        assertThat(departmentResponseList.size()).isEqualTo(returnDepartmentResponseList.size());
        assertThat(departmentResponseList.get(0).getId()).isEqualTo(returnDepartmentResponseList.get(0).getId());
        assertThat(departmentResponseList.get(0).getName()).isEqualTo(returnDepartmentResponseList.get(0).getName());
    }
}