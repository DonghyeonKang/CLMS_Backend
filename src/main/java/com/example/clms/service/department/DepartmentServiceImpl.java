package com.example.clms.service.department;

import com.example.clms.dto.department.DepartmentResponse;
import com.example.clms.entity.department.Department;
import com.example.clms.repository.department.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService{
    private final DepartmentRepository departmentRepository;

    @Override
    public List<DepartmentResponse> findAllDepartment(int universityId) {

        List<Department> entityList = departmentRepository.findAllByUniversity_Id(universityId);
        List<DepartmentResponse> dtoList = new ArrayList<>();

        // entity List 를 dto List 로 변환
        for (Department entity : entityList) {
            dtoList.add(entity.toDto());
        }

        return dtoList;
    }
}
