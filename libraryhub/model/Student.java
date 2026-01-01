package com.libraryhub.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true , nullable = false , length = 20)
    private String studentId;
    @Column(length = 30)
    private String name;
    @Column(unique = true,nullable = false)
    private String email;
    @Column(unique = true ,length = 20)
    private String mobile;
    @Column(length = 12)
    private String studentClass;
    private LocalDate registrationDate;

    @OneToMany(mappedBy = "student" , fetch = FetchType.EAGER)
    private List<BookIssue> bookIssueList;
}
