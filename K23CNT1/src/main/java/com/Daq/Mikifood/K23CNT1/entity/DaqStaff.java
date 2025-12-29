package com.Daq.Mikifood.K23CNT1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "staff")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DaqStaff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "staff_id")
    private Integer staffId;

    @Column(name = "staff_name", length = 150)
    private String staffName;

    @Column(name = "phone", length = 15)
    private String phone;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "role", length = 50)
    private String role;

    @Column(name = "username", unique = true, length = 50)
    private String username;

    @Column(name = "password", length = 255)
    private String password;

    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL)
    private List<DaqOrder> orders;
}
