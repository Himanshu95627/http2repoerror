package com.WebSocketWithHttp.WebsocketWithHttp.RestModels;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;

@Getter
@Setter
@ToString
@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private long age;
    @Column(name = "location")
    private String location;
    @Column(name = "email")
    private String email;
    @Column(name = "department")
    private String department;
}
