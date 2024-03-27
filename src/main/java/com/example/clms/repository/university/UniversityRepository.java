package com.example.clms.repository.university;


import com.example.clms.entity.university.University;

import java.util.List;

public interface UniversityRepository {
    List<University> findAll();
    University findById(int universityId);
    University getReferenceById(int universityId);

}
