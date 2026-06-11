
package com.example.volunteer.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberUpdateRequest {

    private String name;

    private String gender;

    private String grade;

    private String college;

    private String major;

    private String phone;

    private String email;

    private LocalDate joinDate;

    private String status;
}
