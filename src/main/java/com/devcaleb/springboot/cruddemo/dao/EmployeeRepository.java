package com.devcaleb.springboot.cruddemo.dao;

import com.devcaleb.springboot.cruddemo.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
