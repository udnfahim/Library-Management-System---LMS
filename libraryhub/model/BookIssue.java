package com.libraryhub.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookIssues")
public class BookIssue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.EAGER)
    private Book book;
    @ManyToOne(fetch = FetchType.EAGER)
    private Student student;
    private LocalDate issueDate;
    private LocalDate returnDate;

    /*
   Disclosure: Parts of this code were written with the help of AI-generated references
        and later reviewed, modified, and integrated by the developer.
    */
    @Enumerated(EnumType.STRING)
    private BookIssueStatus status;
}
