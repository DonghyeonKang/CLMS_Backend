package com.example.clms.repository.department;

import com.example.clms.entity.department.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaDepartmentRepository extends JpaRepository<Department, Long>, DepartmentRepository {
    @Override
    public List<Department> findAllByUniversity_Id(int universityId);

    @Override
    public Department getReferenceById(Long departmentId);
}
