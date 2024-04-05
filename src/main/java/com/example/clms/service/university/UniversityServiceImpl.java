package com.example.clms.service.university;

import com.example.clms.dto.university.UniversityDto;
import com.example.clms.entity.university.University;
import com.example.clms.repository.university.UniversityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UniversityServiceImpl implements UniversityService{
    private final UniversityRepository universityRepository;

    public List<UniversityDto> findAllUniversity() {

        List<University> entityList = universityRepository.findAll();
        List<UniversityDto> dtoList = new ArrayList<>();

        for (University entity : entityList) {
            dtoList.add(entity.toDto());
        }

        return dtoList;
    }

    public University findById(int universityId) {
        return universityRepository.findById(universityId);
    }
}
