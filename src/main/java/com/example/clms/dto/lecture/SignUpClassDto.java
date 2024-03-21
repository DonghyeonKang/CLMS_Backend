package com.example.clms.dto.lecture;

import lombok.*;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpClassDto {
    private Long userId;
    private Long lectureId;
}
