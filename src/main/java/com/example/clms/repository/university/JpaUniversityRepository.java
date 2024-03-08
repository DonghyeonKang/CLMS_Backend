package com.example.clms.repository.university;

import com.example.clms.entity.university.University;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaUniversityRepository extends JpaRepository<University, Long>, UniversityRepository {
    public List<University> findAll();
    public University getReferenceById(int universityId);

}
