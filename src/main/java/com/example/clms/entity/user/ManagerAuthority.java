package com.example.clms.entity.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicUpdate
public class ManagerAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String status;

    public void acceptAuthorityStatus() {
        this.status = "accepted";
    }
    public void denyAuthorityStatus() {
        this.status = "denied";
    }
}
