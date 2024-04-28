package com.atharva.employwise.service;

import com.atharva.employwise.dto.EmployeeRequestDTO;
import com.atharva.employwise.exception.EmployeeException;
import com.atharva.employwise.model.Employee;
import com.atharva.employwise.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {


    private final EmployeeRepository employeeRepository;
    private final EmailSendingExecutor emailSendingExecutor;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmailSendingExecutor emailSendingExecutor) {
        this.employeeRepository = employeeRepository;
        this.emailSendingExecutor = emailSendingExecutor;
    }


    @Override
    @Transactional(readOnly = true)
    public Employee getEmployeeById(String id) throws EmployeeException {
        try {
            return employeeRepository.getEmployeeById(id);
        } catch (Exception e) {
            throw new EmployeeException("Failed to retrieve employee by ID: " + id, e);
        }
    }

    @Override
    @Transactional
    public UUID addEmployee(EmployeeRequestDTO employeeRequestDTO) throws EmployeeException {
        try {

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

        } catch (Exception e) {
            throw new EmployeeException("Failed to add employee", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> getAllEmployees() throws EmployeeException {
        try {
            return employeeRepository.getAllEmployees();
        } catch (Exception e) {
            throw new EmployeeException("Failed to retrieve all employees", e);
        }
    }

    @Override
    @Transactional
    public void deleteEmployeeById(String id) throws EmployeeException {
        try {
            employeeRepository.deleteById(id);
        } catch (NoSuchElementException e) {
            throw new EmployeeException("Employee with ID " + id + " not found", e);
        } catch (Exception e) {
            throw new EmployeeException("Failed to delete employee with ID " + id, e);
        }
    }

    @Override
    @Transactional
    public void updateEmployee(Employee updatedEmployee) throws EmployeeException {
        try {
            employeeRepository.update(updatedEmployee);
        } catch (Exception e) {
            throw new EmployeeException("Failed to update employee", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> getAll(int pageNumber, int pageSize, String sortBy) throws EmployeeException {
        try {
            return employeeRepository.getAll(pageNumber, pageSize, sortBy);
        } catch (Exception e) {
            throw new EmployeeException("Failed to retrieve employees with pagination and sorting", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Employee getNthLevelManager(String employeeId, int level) throws EmployeeException {
        try {
            if (level <= 0) {
                throw new IllegalArgumentException("Level must be a positive integer");
            }
            Employee employee = employeeRepository.getEmployeeById(employeeId);
            while (level > 0 && employee != null) {
                employee = employeeRepository.getEmployeeById(String.valueOf(employee.getReportsTo()));
                level--;
            }
            if (employee == null) {
                throw new NoSuchElementException("No manager found at the specified level");
            }

            return employee;

        } catch (Exception e) {
            throw new EmployeeException("Failed to retrieve nth level manager for employee with ID " + employeeId, e);
        }
    }
}

