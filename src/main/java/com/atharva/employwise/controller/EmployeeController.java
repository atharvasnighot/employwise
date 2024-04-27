package com.atharva.employwise.controller;

import com.atharva.employwise.dto.EmployeeRequestDTO;
import com.atharva.employwise.dto.EmployeeResponseDTO;
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
    public ResponseEntity<String> getEmployeeName(@PathVariable String id){

        Employee employee = employeeService.getEmployeeById(id);
        String name = employee.getEmployeeName();

        return new ResponseEntity<>(name, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<EmployeeResponseDTO> addEmployee(@RequestBody EmployeeRequestDTO employeeRequestDTO) {
        UUID employeeId = employeeService.addEmployee(employeeRequestDTO);
        return new ResponseEntity<>(new EmployeeResponseDTO(employeeId), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Employee>> getAll(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "employeeName") String sortBy) {

        List<Employee> employees = employeeService.getAll(pageNumber, pageSize, sortBy);

        return ResponseEntity.ok().body(employees);
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployeeById(@PathVariable String id) {
        try {
            employeeService.deleteEmployeeById(id);
            return ResponseEntity.ok("Employee with ID " + id + " deleted successfully.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateEmployeeById(@PathVariable("id") String id, @RequestBody Employee updatedEmployee) {
        try {
            Employee existingEmployee = employeeService.getEmployeeById(id);
            if (existingEmployee == null) {
                return ResponseEntity.notFound().build();
            }

            updatedEmployee.setId(existingEmployee.getId()); // Ensure the ID remains the same
            employeeService.updateEmployee(updatedEmployee);

            return new ResponseEntity<>("Employee Updated Successfully", HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update employee");
        }
    }

    @GetMapping("/{employeeId}/manager")
    public ResponseEntity<Employee> getNthLevelManager(
            @PathVariable String employeeId,
            @RequestParam int level
    ) {
        try {
            Employee manager = employeeService.getNthLevelManager(employeeId, level);
            return ResponseEntity.ok(manager);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}


