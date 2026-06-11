
package com.example.volunteer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "member_team", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"member_id", "team_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "team_id", nullable = false)
    private Long teamId;

    @Column(name = "join_date", nullable = false)
    private LocalDate joinDate;

    @Column(length = 20)
    private String role = "MEMBER";

    @PrePersist
    protected void onCreate() {
        joinDate = LocalDate.now();
    }
}
