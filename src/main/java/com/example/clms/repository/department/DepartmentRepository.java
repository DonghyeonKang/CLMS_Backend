package com.example.clms.repository.department;


import com.example.clms.entity.department.Department;
import java.util.List;

public interface DepartmentRepository {

    List<Department> findAllByUniversity_Id(int universityId);
    Department getReferenceById(Long departmentId);
}
