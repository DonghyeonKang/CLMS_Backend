package com.example.clms.service.university;

import com.example.clms.dto.university.UniversityDto;
import com.example.clms.entity.university.University;

import java.util.List;

public interface UniversityService {

    List<UniversityDto> findAllUniversity();
    University findById(int UniversityId);
}
