package com.libraryhub.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(length = 120)
    private String name;
    @Column(unique = true , length = 40 , updatable = false)
    private String username ;
    @Column(unique = true , length = 254 , nullable = false)
    private String email ;
    @Column(length = 130)
    private String password;
    @Column(nullable = true)
    private String photoUrl;
    @Column(updatable = false , nullable = false)
    private LocalDateTime registerDate;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
