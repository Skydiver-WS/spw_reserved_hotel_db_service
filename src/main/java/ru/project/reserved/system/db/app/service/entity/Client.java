//package ru.project.reserved.system.db.app.service.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.util.List;
//
//@Entity
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class Client {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    private String firstName;
//    private String middleName;
//    private String lastName;
//    private String email;
//    private String phone;
//    private Integer series;
//    private Integer number;
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Photo> documents;
//}
