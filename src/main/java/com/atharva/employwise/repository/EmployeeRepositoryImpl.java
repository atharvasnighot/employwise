package com.atharva.employwise.repository;

import com.atharva.employwise.model.Employee;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.json.JsonArray;
import com.couchbase.client.java.query.QueryOptions;
import com.couchbase.client.java.query.QueryScanConsistency;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class EmployeeRepositoryImpl implements EmployeeRepository {

    private final Cluster cluster;
    private final Collection employeeCol;

    public EmployeeRepositoryImpl(Cluster cluster, Bucket bucket) {
        this.cluster = cluster;
        this.employeeCol = bucket.scope("EmployeeScope").collection("Employees");
    }

    @Override
    public Employee getEmployeeById(String id){
        return employeeCol.get(id).contentAs(Employee.class);
    }

    @Override
    public Employee save(Employee employee){
        UUID id = UUID.randomUUID();
        employee.setId(id);
        employeeCol.upsert(id.toString(), employee);
        return employee;
    }

    @Override
    public List<Employee> getAllEmployees() {
        String statement = "SELECT * FROM `" + employeeCol.bucketName() + "`.`EmployeeScope`.`Employees`";
        return cluster
                .query(statement,
                        QueryOptions.queryOptions()
                                .scanConsistency(QueryScanConsistency.REQUEST_PLUS))
                .rowsAs(Employee.class);
    }

    @Override
    public List<Employee> getAll(int pageNumber, int pageSize, String sortBy) {
        String statement = "SELECT * FROM `" + employeeCol.bucketName() + "`.`EmployeeScope`.`Employees`";

        // Add sorting criteria if specified
        if (sortBy != null && !sortBy.isEmpty()) {
            statement += " ORDER BY " + sortBy;
        }

        // Add pagination parameters
        statement += " LIMIT $1 OFFSET $2";

        return cluster
                .query(statement,
                        QueryOptions.queryOptions().parameters(JsonArray.from(pageSize, (pageNumber - 1) * pageSize))
                                .scanConsistency(QueryScanConsistency.REQUEST_PLUS))
                .rowsAs(Employee.class);
    }


    @Override
    public void deleteById(String id) {
        employeeCol.remove(id);
    }

    @Override
    public void update(Employee employee) {
        employeeCol.replace(employee.getId().toString(), employee);
    }



}

