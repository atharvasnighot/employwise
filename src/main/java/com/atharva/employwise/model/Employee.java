package com.atharva.employwise.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nonapi.io.github.classgraph.json.Id;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Employee {

    @Id
    private UUID id;

    private String employeeName;
    private String phoneNumber;
    private String email;
    private UUID reportsTo;
    private String profileImage;

    @JsonProperty("Employees")
    private void unpackNested(JsonNode employeeNode) {
        this.id = UUID.fromString(employeeNode.get("id").textValue());
        this.employeeName = employeeNode.get("employeeName").textValue();
        this.phoneNumber = employeeNode.get("phoneNumber").textValue();
        this.email = employeeNode.get("email").textValue();
        this.reportsTo = UUID.fromString(employeeNode.get("reportsTo").textValue());
        this.profileImage = employeeNode.get("profileImage").textValue();
    }

}
