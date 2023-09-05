package com.WebSocketWithHttp.WebsocketWithHttp.RestRegistory;

import com.WebSocketWithHttp.WebsocketWithHttp.RestModels.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRegistory extends JpaRepository<Employee,Long> {
}
