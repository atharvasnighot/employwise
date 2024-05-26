package com.atharva.employwise.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.atharva.employwise.dto.EmployeeRequestDTO;
import com.atharva.employwise.dto.EmployeeResponseDTO;
import com.atharva.employwise.exception.EmployeeException;
import com.atharva.employwise.model.Employee;
import com.atharva.employwise.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetEmployeeName() throws EmployeeException {
        String employeeId = "1";
        Employee employee = new Employee();
        employee.setEmployeeName("John Doe");

        when(employeeService.getEmployeeById(employeeId)).thenReturn(employee);

        ResponseEntity<String> response = employeeController.getEmployeeName(employeeId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("John Doe", response.getBody());

        verify(employeeService, times(1)).getEmployeeById(employeeId);
    }

    @Test
    void testAddEmployee() throws EmployeeException {
        EmployeeRequestDTO employeeRequestDTO = new EmployeeRequestDTO();
        UUID employeeId = UUID.randomUUID();

        when(employeeService.addEmployee(any(EmployeeRequestDTO.class))).thenReturn(employeeId);

        ResponseEntity<EmployeeResponseDTO> response = employeeController.addEmployee(employeeRequestDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(employeeId, response.getBody().getId());

        verify(employeeService, times(1)).addEmployee(any(EmployeeRequestDTO.class));
    }

    @Test
    void testGetAll() throws EmployeeException {
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());

        when(employeeService.getAll(anyInt(), anyInt(), anyString())).thenReturn(employees);

        ResponseEntity<List<Employee>> response = employeeController.getAll(1, 10, "employeeName");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employees, response.getBody());

        verify(employeeService, times(1)).getAll(anyInt(), anyInt(), anyString());
    }

    @Test
    void testGetAllEmployees() throws EmployeeException {
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());

        when(employeeService.getAllEmployees()).thenReturn(employees);

        ResponseEntity<List<Employee>> response = employeeController.getAllEmployees();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employees, response.getBody());

        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    void testDeleteEmployeeById() throws EmployeeException {
        String employeeId = "1";

        doNothing().when(employeeService).deleteEmployeeById(employeeId);

        ResponseEntity<String> response = employeeController.deleteEmployeeById(employeeId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Employee with ID 1 deleted successfully.", response.getBody());

        verify(employeeService, times(1)).deleteEmployeeById(employeeId);
    }

    @Test
    void testDeleteEmployeeByIdNotFound() throws EmployeeException {
        String employeeId = "1";

        doThrow(NoSuchElementException.class).when(employeeService).deleteEmployeeById(employeeId);

        ResponseEntity<String> response = employeeController.deleteEmployeeById(employeeId);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(employeeService, times(1)).deleteEmployeeById(employeeId);
    }

    @Test
    void testUpdateEmployeeById() throws EmployeeException {
        String employeeId = "1";
        Employee updatedEmployee = new Employee();

        Employee existingEmployee = new Employee();
        existingEmployee.setId(UUID.randomUUID());

        when(employeeService.getEmployeeById(employeeId)).thenReturn(existingEmployee);
        doNothing().when(employeeService).updateEmployee(any(Employee.class));

        ResponseEntity<String> response = employeeController.updateEmployeeById(employeeId, updatedEmployee);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Employee Updated Successfully", response.getBody());

        verify(employeeService, times(1)).getEmployeeById(employeeId);
        verify(employeeService, times(1)).updateEmployee(any(Employee.class));
    }

    @Test
    void testUpdateEmployeeByIdNotFound() throws EmployeeException {
        String employeeId = "1";
        Employee updatedEmployee = new Employee();

        when(employeeService.getEmployeeById(employeeId)).thenReturn(null);

        ResponseEntity<String> response = employeeController.updateEmployeeById(employeeId, updatedEmployee);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(employeeService, times(1)).getEmployeeById(employeeId);
        verify(employeeService, never()).updateEmployee(any(Employee.class));
    }

    @Test
    void testGetNthLevelManager() throws EmployeeException {
        String employeeId = "1";
        int level = 2;
        Employee manager = new Employee();

        when(employeeService.getNthLevelManager(employeeId, level)).thenReturn(manager);

        ResponseEntity<Employee> response = employeeController.getNthLevelManager(employeeId, level);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(manager, response.getBody());

        verify(employeeService, times(1)).getNthLevelManager(employeeId, level);
    }

    @Test
    void testGetNthLevelManagerNotFound() throws EmployeeException {
        String employeeId = "1";
        int level = 2;

        when(employeeService.getNthLevelManager(employeeId, level)).thenThrow(NoSuchElementException.class);

        ResponseEntity<Employee> response = employeeController.getNthLevelManager(employeeId, level);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(employeeService, times(1)).getNthLevelManager(employeeId, level);
    }

    @Test
    void testGetNthLevelManagerBadRequest() throws EmployeeException {
        String employeeId = "1";
        int level = 2;

        when(employeeService.getNthLevelManager(employeeId, level)).thenThrow(IllegalArgumentException.class);

        ResponseEntity<Employee> response = employeeController.getNthLevelManager(employeeId, level);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        verify(employeeService, times(1)).getNthLevelManager(employeeId, level);
    }

    @Test
    void testGetNthLevelManagerServerError() throws EmployeeException {
        String employeeId = "1";
        int level = 2;

        when(employeeService.getNthLevelManager(employeeId, level)).thenThrow(RuntimeException.class);

        ResponseEntity<Employee> response = employeeController.getNthLevelManager(employeeId, level);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        verify(employeeService, times(1)).getNthLevelManager(employeeId, level);
    }
}
