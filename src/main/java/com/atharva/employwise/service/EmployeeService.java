package com.atharva.employwise.service;

import com.atharva.employwise.dto.EmployeeRequestDTO;
import com.atharva.employwise.model.Employee;

import java.util.List;
import java.util.UUID;


public interface EmployeeService {

    Employee getEmployeeById(String id);

    UUID addEmployee(EmployeeRequestDTO employeeRequestDTO);

    List<Employee> getAllEmployees();

    void deleteEmployeeById(String id);

    void updateEmployee(Employee updatedEmployee);

    List<Employee> getAll(int pageNumber, int pageSize, String sortBy);

    Employee getNthLevelManager(String employeeId, int level);
}
