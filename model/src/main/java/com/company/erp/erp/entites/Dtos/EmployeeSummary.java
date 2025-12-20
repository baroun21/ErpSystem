package com.company.erp.erp.entites.Dtos;
import com.company.erp.erp.entites.response.AttendanceResponse;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeSummary {
    private Long employeeId;
    private String firstName;
    private String lastName;
    private String jobTitle;
    private String email;
    private AttendanceResponse attendance;


}

