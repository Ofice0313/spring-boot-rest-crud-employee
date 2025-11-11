package com.devcaleb.springboot.cruddemo.controllers;

import com.devcaleb.springboot.cruddemo.entities.Employee;
import com.devcaleb.springboot.cruddemo.services.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class EmployeeController {

    private EmployeeService employeeService;

    private ObjectMapper objectMapper;

    @Autowired
    public EmployeeController(EmployeeService employeeService, ObjectMapper objectMapper) {
        this.employeeService = employeeService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/employees")
    public List<Employee> findAll() {
        return employeeService.findAll();
    }

    // add mapping for GET /employees/{employeeId}
    @GetMapping("/employees/{employeeId}")
    public Employee getEmployee(@PathVariable int employeeId) {
        Employee employee = employeeService.findById(employeeId);

        if(employee == null) {
            throw new RuntimeException("Employee id not foundb -> " + employeeId);
        }
        return employee;
    }

    // add mapping for POST /employees - add new employee
    @PostMapping("/employees")
    public Employee addEmployee(@RequestBody Employee employee){

        // also just in case they pass an id in JSON ... set id to 0
        // this is to force a  save of new item ... instead of update

        employee.setId(null);
        Employee dbEmployee = employeeService.save(employee);
        return dbEmployee;
    }

    // add mapping for PUT /employees - update existing employee

    @PutMapping("/employees")
    public Employee updateEmployee(@RequestBody Employee employee) {
        Employee dbEmployee = employeeService.save(employee);
        return dbEmployee;
    }

    // add mapping for PATCH /employee/{employeeId} - patch employee ... partial update
    @PatchMapping("/employees/{employeeId}")
    public Employee patchEmployee(@PathVariable int employeeId,
                                  @RequestBody Map<String, Object> patchPayload) {
        Employee tempEmployee = employeeService.findById(employeeId);

        // throw exception if null
        if(tempEmployee == null ) {
            throw new RuntimeException("Employee not found -> " + employeeId);
        }

        // throw exception if request body contains "id" key
        if(patchPayload.containsKey("id")) {
            throw new RuntimeException("Employee id not allowed in request body -> " + employeeId);
        }

        Employee patchedEmployee = apply(patchPayload, tempEmployee);
        Employee dbEmployee = employeeService.save(patchedEmployee);
        return dbEmployee;
    }

    private Employee apply(Map<String, Object> patchPayload, Employee tempEmployee) {

        // Convert employee object to a JSON object node
        ObjectNode employeeNode = objectMapper.convertValue(tempEmployee, ObjectNode.class);

        // convert the patchPayload map to a JSON object
        ObjectNode patchNode = objectMapper.convertValue(patchPayload, ObjectNode.class);

        // Merge the patch updates into the employee Node
        employeeNode.setAll(patchNode);

        return objectMapper.convertValue(employeeNode, Employee.class);
    }

    // add mapping for DELETE /employees/{employeeId} - delete employee
    @DeleteMapping("/employees/{employeeId}")
    public String deleteEmployee(@PathVariable int employeeId) {
        Employee tempEmployee = employeeService.findById(employeeId);

        // throw exception if null
        if(tempEmployee == null) {
            throw new RuntimeException("employee id not found -> " + employeeId);
        }
        employeeService.deleteById(employeeId);
        return "Deleted employee id - " + employeeId;
    }
}
