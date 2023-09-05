package com.WebSocketWithHttp.WebsocketWithHttp.RestService;

import com.WebSocketWithHttp.WebsocketWithHttp.RestModels.Employee;
import com.WebSocketWithHttp.WebsocketWithHttp.RestRegistory.EmployeeRegistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService implements EmployeeServiceInterface{
//    public static List<Employee>list = new ArrayList<>();
//    static {
//        Employee employee = new Employee(1,"him",22,"ind","123@email.com","CS");
//        list.add(employee);
//        employee = new Employee(2,"some name",22,"uAE","123@ewremail.com","Backend");
//        list.add(employee);
//
//    }
    @Autowired
    private EmployeeRegistory employeeRegistory;
    @Override
    public List<Employee> getEmployees() {
       return employeeRegistory.findAll();
    }
    @Override
    public Employee createEmployee(Employee employee){
       return employeeRegistory.save(employee);
    }

    @Override
    public Employee getEmployeeByID(Long id) {
        Optional<Employee> employee = employeeRegistory.findById(id);
        if(employee.isPresent()){
            return employee.get();
        }
        throw new RuntimeException(String.format("You are trying to access the data for id:= %s which does not exist in your database!!!",id));
    }

    @Override
    public Employee updateEmployee(Employee employee) {
        Optional<Employee> employee1 = employeeRegistory.findById(employee.getId());
        if(employee1.isPresent()){
            return  employeeRegistory.save(employee);
        }
        throw new RuntimeException(String.format("You are trying to update the data for id:= %s which does not exist in your database!!!",employee.getId()));
    }

    @Override
    public String deleteEmployeeByID(Long id) {
        Optional<Employee> employee = employeeRegistory.findById(id);
        if(employee.isPresent()){
            employeeRegistory.deleteById(id);
            return String.format("Deleted the entry associated with id:= %s",id);
        }
        throw new RuntimeException(String.format("You are trying to access the data for id:= %s which does not exist in your database!!!",id));
    }
}
