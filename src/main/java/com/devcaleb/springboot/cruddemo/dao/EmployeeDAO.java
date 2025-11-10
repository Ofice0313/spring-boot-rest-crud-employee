package com.devcaleb.springboot.cruddemo.dao;

import com.devcaleb.springboot.cruddemo.entities.Employee;

import java.util.List;

public interface EmployeeDAO {

    List<Employee> findAll();

    Employee findById(int id);

    Employee save(Employee employee);

    void deleteById(int id);
}
