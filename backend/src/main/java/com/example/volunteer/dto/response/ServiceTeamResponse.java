
package com.example.volunteer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceTeamResponse {

    private Long id;
    private String name;
    private String description;
    private String icon;
    private String status;
}
