package com.devcaleb.springboot.cruddemo.dao;

import com.devcaleb.springboot.cruddemo.entities.Employee;

import java.util.List;

public interface EmployeeDAO {

    List<Employee> findAll();
}
