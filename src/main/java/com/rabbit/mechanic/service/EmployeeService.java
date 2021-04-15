package com.rabbit.mechanic.service;

import com.rabbit.mechanic.command.Paginated;
import com.rabbit.mechanic.command.employee.CreateEmployeeDto;
import com.rabbit.mechanic.command.employee.EmployeeDetailsDto;
import com.rabbit.mechanic.command.employee.UpdateEmployeeDto;
import com.rabbit.mechanic.exception.employee.EmployeeAlreadyExistsException;
import com.rabbit.mechanic.exception.employee.EmployeeNotFoundException;

/**
 * Common interface for employee services, provides methods to manage employees
 */
public interface EmployeeService {

    /**
     * Create new employee
     * @param createEmployeeDto {@link CreateEmployeeDto}
     * @return {@link EmployeeDetailsDto} the employee created
     * @throws EmployeeAlreadyExistsException when the employee already exists
     */
    EmployeeDetailsDto createEmployee(CreateEmployeeDto createEmployeeDto) throws EmployeeAlreadyExistsException;

    /**
     * Get employee by id
     * @param employeeId employee id we want to get
     * @return {@link EmployeeDetailsDto} the employee obtained
     * @throws EmployeeNotFoundException when the employee aren't found
     */
    EmployeeDetailsDto getEmployeeById(long employeeId) throws EmployeeNotFoundException;

    /**
     * Get all employees
     * @return {@link EmployeeDetailsDto} the employees obtained
     */
    Paginated<EmployeeDetailsDto> getEmployeesList(int page, int size);

    /**
     * Update employee
     * @param employeeId employee id we want to update
     * @param updateEmployeeDto {@link UpdateEmployeeDto}
     * @return {@link EmployeeDetailsDto} the employee updated
     * @throws EmployeeNotFoundException when the employee aren't found
     */
    EmployeeDetailsDto updateEmployee(long employeeId, UpdateEmployeeDto updateEmployeeDto) throws EmployeeNotFoundException;

    /**
     * Delete employee
     * @param workerId employee id we want to delete
     * @throws EmployeeNotFoundException when the employee aren't found
     */
    void deleteEmployee(long workerId) throws EmployeeNotFoundException;
}
