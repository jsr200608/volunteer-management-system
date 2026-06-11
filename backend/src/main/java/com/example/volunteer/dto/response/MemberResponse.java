
package com.example.volunteer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse {

    private Long id;
    private String studentId;
    private String name;
    private String gender;
    private String grade;
    private String college;
    private String major;
    private String phone;
    private String email;
    private BigDecimal volunteerHours;
    private LocalDate joinDate;
    private String status;
}
