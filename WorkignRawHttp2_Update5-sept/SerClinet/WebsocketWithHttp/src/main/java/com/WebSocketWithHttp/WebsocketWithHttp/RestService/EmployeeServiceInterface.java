package com.WebSocketWithHttp.WebsocketWithHttp.RestService;

import com.WebSocketWithHttp.WebsocketWithHttp.RestModels.Employee;

import java.util.List;

public interface EmployeeServiceInterface {
    List<Employee> getEmployees();
    Employee createEmployee(Employee employee);
    Employee getEmployeeByID(Long id);

    Employee updateEmployee(Employee employee);

    String deleteEmployeeByID(Long id);
}
