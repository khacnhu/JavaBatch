package com.example.BatchProcessing.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customer_batch")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "gender")
    private String gender;

    @Column(name = "contact_number")
    private Integer contactNo;

    @Column(name = "country")
    private String country;

//    @Column(name = "dob")
//    private String dob;

//    @Id
//    @GeneratedValue
//    private Integer id;
//
//
//    private String firstname;
//
//    private String lastname;
//
//    private int age;


}
