package com.example.crudapi.services;

import com.example.crudapi.entities.Employee;
import com.example.crudapi.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;
    @Override
    public void saveEmployee(Employee u) {employeeRepository.save(u);}

    @Override
    public Employee findById(Integer id) {
        Optional<Employee> employees = employeeRepository.findById(id);
        Employee employee = employees.get();
        return employee;
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }
}
