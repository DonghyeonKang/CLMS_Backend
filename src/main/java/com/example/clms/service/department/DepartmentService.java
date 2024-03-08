package com.example.clms.service.department;

import com.example.clms.dto.department.DepartmentResponse;

import java.util.List;

public interface DepartmentService {
    List<DepartmentResponse> findAllDepartment(int universityId);
}
