package com.example.clms.dto.lecture;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostingNoticeRequest {
    private String title;
    private String content;
    private Long lectureId;
    private String createAt;
}
