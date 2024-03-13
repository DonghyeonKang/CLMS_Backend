package com.example.clms.entity.auth;

import com.example.clms.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String token;

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "User_id")
    private User user;

    public void updateToken(String token) {
        this.token = token;
    }
}
