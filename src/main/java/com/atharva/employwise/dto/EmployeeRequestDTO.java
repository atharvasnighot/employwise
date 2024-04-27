package com.atharva.employwise.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class EmployeeRequestDTO {
    private String employeeName;
    private String phoneNumber;
    private String email;
    private UUID reportsTo;
    private String profileImage;
}

