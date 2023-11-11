package com.example.crudapi.controllers;

import com.example.crudapi.entities.Employee;
import com.example.crudapi.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    @RequestMapping(value = "employee", method = RequestMethod.GET)
    public ResponseEntity<List<Employee>> getEmployees() {
        List<Employee> listEmployee = employeeService.findAll();
        if(listEmployee.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(listEmployee, HttpStatus.OK);
    }

    @RequestMapping(value = "employee", method = RequestMethod.POST)
    public ResponseEntity<Employee> addEmployees(@RequestBody Employee u) {
        employeeService.saveEmployee(u);
        return new ResponseEntity<Employee>(u, HttpStatus.OK);
    }

    @RequestMapping(value = "employee/{id}", method = RequestMethod.GET)
    public ResponseEntity<Employee> findEmployee(@PathVariable(value = "id") Integer id) {
        Employee employee = employeeService.findById(id);
        return new ResponseEntity<Employee>(employee, HttpStatus.OK);
    }

    @RequestMapping(value = "employee/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable(value="id") Integer id,
            @RequestBody Employee u) {
        Employee oldEmployee = employeeService.findById(id);
        oldEmployee.setName(u.getName());
        oldEmployee.setSalary(u.getSalary());
        employeeService.saveEmployee(oldEmployee);
        return new ResponseEntity<Employee>(oldEmployee, HttpStatus.OK);
    }
}
