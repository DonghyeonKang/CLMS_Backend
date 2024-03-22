package com.example.clms.dto.instance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OwnerInstanceRequest {

    private String username;
    private int instanceId;

}
