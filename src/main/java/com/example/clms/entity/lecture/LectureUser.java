package com.example.clms.entity.lecture;

import com.example.clms.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class LectureUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "User_id")
    private User user;

    @ManyToOne(targetEntity = Lecture.class)
    @JoinColumn(name = "Lecture_id")
    private Lecture lecture;

    @Builder.Default()
    private String permit = "waiting";

    public void setPermit() {
        this.permit = "permit";
    }

    public void setRefuse() {
        this.permit = "refuse";
    }
}
