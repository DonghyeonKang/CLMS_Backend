package com.example.clms.entity.user;

import com.example.clms.entity.auth.RefreshToken;
import com.example.clms.entity.department.Department;
import com.example.clms.entity.instance.Instance;
import com.example.clms.entity.lecture.LectureUser;
import com.example.clms.entity.university.University;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Users")
@Data
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;    // 아이디
    private String password;    // 비밀번호
    private String name;    // 이름
    private int no;  // 학번 직번
    @Enumerated(EnumType.STRING)
    private Roles roles; // 사용자 권한
    private String phone;
    @ManyToOne(targetEntity = University.class) // university 엔티티와 관계를 설정할 것임을 명시
    @JoinColumn(name = "university_id") // university 엔티티의 id 값으로 칼럼을 join 함. join 한 칼럼명 설정도 할 수 있는데 기본인 university_id 로 사용
    private University university;
    @ManyToOne(targetEntity = Department.class)
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Instance> instanceList = new ArrayList<>();
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<LectureUser> lectureUserList = new ArrayList<>();
    @OneToOne(mappedBy = "user", orphanRemoval = true)
    private RefreshToken refreshToken;

    public SimpleGrantedAuthority getRoleList(){
        return new SimpleGrantedAuthority(this.roles.getRole());
    }

    public void setManager() {
        this.roles = Roles.MANAGER;
    }
}
