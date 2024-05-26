package com.atharva.employwise.service;

import com.atharva.employwise.dto.EmployeeRequestDTO;
import com.atharva.employwise.exception.EmployeeException;
import com.atharva.employwise.model.Employee;
import com.atharva.employwise.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmailSendingExecutor emailSendingExecutor;

    @InjectMocks
    private EmployeeServiceImpl employeeServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetEmployeeById() throws EmployeeException {
        String employeeId = "1";
        Employee employee = new Employee();
        when(employeeRepository.getEmployeeById(employeeId)).thenReturn(employee);

        Employee result = employeeServiceImpl.getEmployeeById(employeeId);

        assertEquals(employee, result);
        verify(employeeRepository, times(1)).getEmployeeById(employeeId);
    }

    @Test
    void testGetEmployeeByIdException() {
        String employeeId = "1";
        when(employeeRepository.getEmployeeById(employeeId)).thenThrow(new RuntimeException("Error"));

        assertThrows(EmployeeException.class, () -> employeeServiceImpl.getEmployeeById(employeeId));
        verify(employeeRepository, times(1)).getEmployeeById(employeeId);
    }

    @Test
    void testAddEmployee() throws EmployeeException {
        EmployeeRequestDTO employeeRequestDTO = new EmployeeRequestDTO();
        employeeRequestDTO.setEmployeeName("John Doe");
        employeeRequestDTO.setPhoneNumber("1234567890");
        employeeRequestDTO.setEmail("john.doe@example.com");
        employeeRequestDTO.setReportsTo(UUID.randomUUID());

        Employee manager = new Employee();
        manager.setEmail("manager@example.com");

        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(employeeRepository.getEmployeeById(anyString())).thenReturn(manager);
        doNothing().when(emailSendingExecutor).sendEmailAsync(anyString(), any(Employee.class));

        UUID result = employeeServiceImpl.addEmployee(employeeRequestDTO);

        assertNotNull(result);
        verify(employeeRepository, times(1)).save(any(Employee.class));
        verify(employeeRepository, times(1)).getEmployeeById(anyString());
        verify(emailSendingExecutor, times(1)).sendEmailAsync(anyString(), any(Employee.class));
    }

    @Test
    void testAddEmployeeException() {
        EmployeeRequestDTO employeeRequestDTO = new EmployeeRequestDTO();
        when(employeeRepository.save(any(Employee.class))).thenThrow(new RuntimeException("Error"));

        assertThrows(EmployeeException.class, () -> employeeServiceImpl.addEmployee(employeeRequestDTO));
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void testGetAllEmployees() throws EmployeeException {
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        when(employeeRepository.getAllEmployees()).thenReturn(employees);

        List<Employee> result = employeeServiceImpl.getAllEmployees();

        assertEquals(employees, result);
        verify(employeeRepository, times(1)).getAllEmployees();
    }

    @Test
    void testGetAllEmployeesException() {
        when(employeeRepository.getAllEmployees()).thenThrow(new RuntimeException("Error"));

        assertThrows(EmployeeException.class, () -> employeeServiceImpl.getAllEmployees());
        verify(employeeRepository, times(1)).getAllEmployees();
    }

    @Test
    void testDeleteEmployeeById() throws EmployeeException {
        String employeeId = "1";
        doNothing().when(employeeRepository).deleteById(employeeId);

        employeeServiceImpl.deleteEmployeeById(employeeId);

        verify(employeeRepository, times(1)).deleteById(employeeId);
    }

    @Test
    void testDeleteEmployeeByIdNotFoundException() {
        String employeeId = "1";
        doThrow(NoSuchElementException.class).when(employeeRepository).deleteById(employeeId);

        assertThrows(EmployeeException.class, () -> employeeServiceImpl.deleteEmployeeById(employeeId));
        verify(employeeRepository, times(1)).deleteById(employeeId);
    }

    @Test
    void testUpdateEmployee() throws EmployeeException {
        Employee updatedEmployee = new Employee();
        doNothing().when(employeeRepository).update(updatedEmployee);

        employeeServiceImpl.updateEmployee(updatedEmployee);

        verify(employeeRepository, times(1)).update(updatedEmployee);
    }

    @Test
    void testUpdateEmployeeException() {
        Employee updatedEmployee = new Employee();
        doThrow(new RuntimeException("Error")).when(employeeRepository).update(updatedEmployee);

        assertThrows(EmployeeException.class, () -> employeeServiceImpl.updateEmployee(updatedEmployee));
        verify(employeeRepository, times(1)).update(updatedEmployee);
    }

    @Test
    void testGetAll() throws EmployeeException {
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        when(employeeRepository.getAll(anyInt(), anyInt(), anyString())).thenReturn(employees);

        List<Employee> result = employeeServiceImpl.getAll(1, 10, "employeeName");

        assertEquals(employees, result);
        verify(employeeRepository, times(1)).getAll(anyInt(), anyInt(), anyString());
    }

    @Test
    void testGetAllException() {
        when(employeeRepository.getAll(anyInt(), anyInt(), anyString())).thenThrow(new RuntimeException("Error"));

        assertThrows(EmployeeException.class, () -> employeeServiceImpl.getAll(1, 10, "employeeName"));
        verify(employeeRepository, times(1)).getAll(anyInt(), anyInt(), anyString());
    }

    @Test
    void testGetNthLevelManager() throws EmployeeException {
        String employeeId = "1";
        int level = 2;
        Employee employee = new Employee();
        employee.setReportsTo(UUID.randomUUID());

        Employee manager = new Employee();

        when(employeeRepository.getEmployeeById(employeeId)).thenReturn(employee);
        when(employeeRepository.getEmployeeById(anyString())).thenReturn(manager);

        Employee result = employeeServiceImpl.getNthLevelManager(employeeId, level);

        assertEquals(manager, result);
        verify(employeeRepository, times(2)).getEmployeeById(anyString());
    }

    @Test
    void testGetNthLevelManagerNoSuchElementException() {
        String employeeId = "1";
        int level = 2;
        Employee employee = new Employee();
        employee.setReportsTo(UUID.randomUUID());

        when(employeeRepository.getEmployeeById(employeeId)).thenReturn(employee);
        when(employeeRepository.getEmployeeById(anyString())).thenReturn(null);

        assertThrows(EmployeeException.class, () -> employeeServiceImpl.getNthLevelManager(employeeId, level));
        verify(employeeRepository, times(2)).getEmployeeById(anyString());
    }

    @Test
    void testGetNthLevelManagerIllegalArgumentException() {
        String employeeId = "1";
        int level = -1;

        assertThrows(IllegalArgumentException.class, () -> employeeServiceImpl.getNthLevelManager(employeeId, level));
        verify(employeeRepository, never()).getEmployeeById(anyString());
    }

    @Test
    void testGetNthLevelManagerException() {
        String employeeId = "1";
        int level = 2;
        when(employeeRepository.getEmployeeById(employeeId)).thenThrow(new RuntimeException("Error"));

        assertThrows(EmployeeException.class, () -> employeeServiceImpl.getNthLevelManager(employeeId, level));
        verify(employeeRepository, times(1)).getEmployeeById(employeeId);
    }
}
