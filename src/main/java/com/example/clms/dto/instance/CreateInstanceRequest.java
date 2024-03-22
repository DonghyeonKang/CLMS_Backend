package com.example.clms.dto.instance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateInstanceRequest {
    private String name;
    private String storage;
    private String keyName;
    private String address;
    private String os;
    private Long lectureId;
}
