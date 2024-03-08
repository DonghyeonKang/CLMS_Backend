package com.example.clms.repository.department;


import com.example.clms.entity.department.Department;
import java.util.List;

public interface DepartmentRepository {

    public List<Department> findAllByUniversity_Id(int universityId);
    public Department getReferenceById(Long departmentId);
}
