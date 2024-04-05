package com.example.clms.service.university;

import com.example.clms.dto.university.UniversityDto;
import com.example.clms.entity.university.University;
import com.example.clms.repository.university.UniversityRepository;
import org.junit.jupiter.api.Assertions;
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
class UniversityServiceImplTest {

    @Mock
    private UniversityRepository universityRepository;

    private UniversityService universityService;

    @BeforeEach
    void setUp() {
        universityService = new UniversityServiceImpl(universityRepository);
    }

    @Test
    void 모든대학조회() {
        // given
        List<University> entityList = new ArrayList<>();
        University university1 = University.builder()
                .id(1)
                .name("경상국립대학교")
                .build();
        University university2 = University.builder()
                .id(1)
                .name("부산대학교")
                .build();
        entityList.add(university1);
        entityList.add(university2);

        when(universityRepository.findAll()).thenReturn(entityList);

        List<UniversityDto> dtoList = new ArrayList<>();
        dtoList.add(university1.toDto());
        dtoList.add(university2.toDto());

        // when
        List<UniversityDto> returnDtoList = universityService.findAllUniversity();

        // then
        assertEquals(dtoList.size(), returnDtoList.size());
        for (int i = 0; i < dtoList.size(); i++) {
            assertEquals(dtoList.get(i).getId(), returnDtoList.get(i).getId());
            assertEquals(dtoList.get(i).getName(), returnDtoList.get(i).getName());
        }
    }

    @Test
    void 대학조회() {
        // given
        University university = University.builder()
                .id(1)
                .name("경상국립대학교")
                .build();

        when(universityRepository.findById(1)).thenReturn(university);

        // when
        University returnUniversity = universityService.findById(1);

        // then
        assertThat(university.getId()).isEqualTo(returnUniversity.getId());
        assertThat(university.getName()).isEqualTo(returnUniversity.getName());
    }
}