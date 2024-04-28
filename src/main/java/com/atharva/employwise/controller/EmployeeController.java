package com.atharva.employwise.controller;

import com.atharva.employwise.dto.EmployeeRequestDTO;
import com.atharva.employwise.dto.EmployeeResponseDTO;
import com.atharva.employwise.exception.EmployeeException;
import com.atharva.employwise.model.Employee;
import com.atharva.employwise.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/api/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @GetMapping("/{id}")
    public ResponseEntity<String> getEmployeeName(@PathVariable String id) throws EmployeeException {

        log.info("Received request to get employee name for ID: {}", id);
        Employee employee = employeeService.getEmployeeById(id);
        String name = employee.getEmployeeName();
        log.info("Successfully retrieved employee name: {}", name);

        return new ResponseEntity<>(name, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<EmployeeResponseDTO> addEmployee(@RequestBody EmployeeRequestDTO employeeRequestDTO) throws EmployeeException {

        log.info("Received request to add employee: {}", employeeRequestDTO);
        UUID employeeId = employeeService.addEmployee(employeeRequestDTO);
        log.info("Successfully added employee with ID: {}", employeeId);

        return new ResponseEntity<>(new EmployeeResponseDTO(employeeId), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Employee>> getAll(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "employeeName") String sortBy) throws EmployeeException {

        log.info("Received request to get all employees");
        List<Employee> employees = employeeService.getAll(pageNumber, pageSize, sortBy);
        log.info("Successfully retrieved all employees");

        return ResponseEntity.ok().body(employees);
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<Employee>> getAllEmployees() throws EmployeeException {

        log.info("Received request to get all employees");
        List<Employee> employees = employeeService.getAllEmployees();
        log.info("Successfully retrieved all employees");

        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEmployeeById(@PathVariable String id) throws EmployeeException {

        log.info("Received request to delete employee with ID: {}", id);
        try {
            employeeService.deleteEmployeeById(id);
            log.info("Successfully deleted employee with ID: {}", id);

            return ResponseEntity.ok("Employee with ID " + id + " deleted successfully.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateEmployeeById(@PathVariable("id") String id, @RequestBody Employee updatedEmployee) {

        log.info("Received request to update employee with ID: {}", id);
        try {
            Employee existingEmployee = employeeService.getEmployeeById(id);
            if (existingEmployee == null) {
                return ResponseEntity.notFound().build();
            }

            updatedEmployee.setId(existingEmployee.getId());
            employeeService.updateEmployee(updatedEmployee);
            log.info("Successfully updated employee with ID: {}", id);

            return new ResponseEntity<>("Employee Updated Successfully", HttpStatus.OK);
        } catch (Exception e) {

            log.error("Failed to update employee with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update employee");
        }
    }

    @GetMapping("/{employeeId}/manager")
    public ResponseEntity<Employee> getNthLevelManager(
            @PathVariable String employeeId,
            @RequestParam int level
    ) {
        log.info("Received request to get nth level manager for employee with ID: {}, level: {}", employeeId, level);
        try {

            Employee manager = employeeService.getNthLevelManager(employeeId, level);
            log.info("Successfully retrieved nth level manager for employee with ID: {}, level: {}", employeeId, level);

            return ResponseEntity.ok(manager);
        } catch (NoSuchElementException e) {

            log.warn("No manager found for employee with ID: {}, level: {}", employeeId, level);
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {

            log.warn("Invalid argument provided for getNthLevelManager: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {

            log.error("Failed to get nth level manager for employee with ID: {}, level: {}", employeeId, level, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
