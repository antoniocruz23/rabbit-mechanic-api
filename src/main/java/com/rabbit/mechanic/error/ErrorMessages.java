package com.rabbit.mechanic.error;

/**
 * Error Messages
 */
public class ErrorMessages {

    public static final String CUSTOMER_NOT_FOUND = "Can't find any customer with the given id";
    public static final String CUSTOMER_ALREADY_EXISTS = "Customer with the given email already exists";

    public static final String EMPLOYEE_NOT_FOUND = "Can't find any employee with the given id";
    public static final String EMPLOYEE_ALREADY_EXISTS = "Employee with the given username already exists";

    public static final String CAR_NOT_FOUND = "Can't find any car with the given id";
    public static final String CAR_ALREADY_EXISTS = "Car with the given plate already exists";

    public static final String REPAIR_NOT_FOUND = "Can't find any repair with the given id";
    public static final String REPAIR_ALREADY_EXISTS = "Repair with the given name already exists";

    public static final String WRONG_CREDENTIALS = "The credentials inserted are wrong";

    public static final String DATABASE_COMMUNICATION_ERROR = "Have some problems with Database communication";
    public static final String OPERATION_FAILED = "Failed to process the requested operation";
}
