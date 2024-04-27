package com.atharva.employwise.repository;

import com.atharva.employwise.model.Employee;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository {

    Employee getEmployeeById(String id);

    Employee save(Employee employee);

    List<Employee> getAllEmployees();

    void deleteById(String id);

    public void update(Employee employee);

    List<Employee> getAll(int pageNumber, int pageSize, String sortBy);
}
