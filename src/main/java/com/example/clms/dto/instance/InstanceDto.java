package com.example.clms.dto.instance;

import com.example.clms.entity.instance.Instance;
import com.example.clms.entity.lecture.Lecture;
import com.example.clms.entity.user.User;
import lombok.*;

import java.sql.Timestamp;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InstanceDto {

    private int instanceId;
    private String name;
    private int code;
    private String state;
    private Double storage;
    private String address;
    private int port;
    private String keyName;
    private String os;
    private Timestamp created;
    private Long userId;
    private Long lectureId;
    private String domainName;
    private String username;

    public Instance toEntity(User user, Lecture lecture) {
        return Instance.builder()
                .id(instanceId)
                .name(name)
                .code(code)
                .state(state)
                .storage(storage)
                .address(address)
                .port(port)
                .keyName(keyName)
                .os(os)
                .created(created)
                .user(user)
                .lecture(lecture)
                .build();
    }
}
