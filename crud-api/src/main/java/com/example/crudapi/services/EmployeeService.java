package com.example.crudapi.services;

import com.example.crudapi.entities.Employee;

import java.util.List;

public interface EmployeeService {
    public void saveEmployee(Employee u);

    public Employee findById(Integer id);
    public List<Employee> findAll();
}
