package com.WebSocketWithHttp.WebsocketWithHttp.rController;

import com.WebSocketWithHttp.WebsocketWithHttp.RestModels.Employee;
import com.WebSocketWithHttp.WebsocketWithHttp.RestService.EmployeeService;
import com.WebSocketWithHttp.WebsocketWithHttp.ServerWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
//@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/rest")
@CrossOrigin(originPatterns = "*")
public class MyRestController {

    @Autowired
    private EmployeeService employeeService;
    @GetMapping("/employees")
    public List<Employee> getEmployees() throws IOException {
        List<Employee> ans = employeeService.getEmployees();
        TextMessage message = new TextMessage("HTTP listing all the employees   "+ans.size());

        for (WebSocketSession session:ServerWebSocketHandler.sessions){
            session.sendMessage(message);
        }
        return ans;
    }

    @GetMapping("/employees/{id}")
    public Employee getEmployeeByID(@PathVariable("id") long id){
        return employeeService.getEmployeeByID(id);
    }
    @PostMapping("/employees")
    public Employee createEmployee(@RequestBody Employee employee){
        return employeeService.createEmployee(employee);
    }
    @PutMapping("/employees/{id}")
    public Employee updateEmployee(@PathVariable("id") long id,
                                @RequestBody Employee employee){
        employee.setId(id);
        return employeeService.updateEmployee(employee);
    }
    ///employees?id=1
    @DeleteMapping("/employees")
    public String deleteEmployeeByID(@RequestParam("id") long id){
        return employeeService.deleteEmployeeByID(id);
    }
}
