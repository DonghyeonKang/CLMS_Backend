package com.example.clms.dto.lecture;

import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApproveRegistrationRequest {
    private Long id;
    private Long lectureId;
}
