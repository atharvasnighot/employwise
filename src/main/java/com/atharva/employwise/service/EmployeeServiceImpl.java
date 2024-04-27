package com.atharva.employwise.service;

import com.atharva.employwise.dto.EmployeeRequestDTO;
import com.atharva.employwise.model.Employee;
import com.atharva.employwise.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    private final EmployeeRepository employeeRepository;
    private EmailSendingExecutor emailSendingExecutor;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmailSendingExecutor emailSendingExecutor) {
        this.employeeRepository = employeeRepository;
        this.emailSendingExecutor = emailSendingExecutor;
    }

    @Override
    public Employee getEmployeeById(String id) {
        return employeeRepository.getEmployeeById(id);
    }

    @Override
    public UUID addEmployee(EmployeeRequestDTO employeeRequestDTO) {
        Employee employee = Employee.builder()
                .id(UUID.randomUUID())
                .employeeName(employeeRequestDTO.getEmployeeName())
                .phoneNumber(employeeRequestDTO.getPhoneNumber())
                .email(employeeRequestDTO.getEmail())
                .reportsTo(employeeRequestDTO.getReportsTo())
                .profileImage(employeeRequestDTO.getProfileImage())
                .build();

        Employee manager = employeeRepository.getEmployeeById(String.valueOf(employee.getReportsTo()));
        emailSendingExecutor.sendEmailAsync(manager.getEmail(), employee);

        return employeeRepository.save(employee).getId();
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.getAllEmployees();
    }

    @Override
    public void deleteEmployeeById(String id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public void updateEmployee(Employee updatedEmployee) {
        employeeRepository.update(updatedEmployee);
    }

    @Override
    public List<Employee> getAll(int pageNumber, int pageSize, String sortBy){
        return employeeRepository.getAll(pageNumber, pageSize, sortBy);
    }

    @Override
    public Employee getNthLevelManager(String employeeId, int level) {
        if (level <= 0) {
            throw new IllegalArgumentException("Level must be a positive integer");
        }
        Employee employee = employeeRepository.getEmployeeById(employeeId);
               // .orElsethrow(new NoSuchElementException());

        // Traverse the hierarchy up to the nth level manager
        while (level > 0 && employee != null) {
            employee = employeeRepository.getEmployeeById(String.valueOf(employee.getReportsTo()));
            //        .orElse(null);
            level--;
        }

        if (employee == null) {
            throw new NoSuchElementException("No manager found at the specified level");
        }

        return employee;
    }


}
