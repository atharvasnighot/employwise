package com.atharva.employwise.service;

import com.atharva.employwise.dto.EmployeeRequestDTO;
import com.atharva.employwise.exception.EmployeeException;
import com.atharva.employwise.model.Employee;

import java.util.List;
import java.util.UUID;


public interface EmployeeService {

    Employee getEmployeeById(String id) throws EmployeeException;

    UUID addEmployee(EmployeeRequestDTO employeeRequestDTO) throws EmployeeException;

    List<Employee> getAllEmployees() throws EmployeeException;

    void deleteEmployeeById(String id) throws EmployeeException;

    void updateEmployee(Employee updatedEmployee) throws EmployeeException;

    List<Employee> getAll(int pageNumber, int pageSize, String sortBy) throws EmployeeException;

    Employee getNthLevelManager(String employeeId, int level) throws EmployeeException;
}
