package com.libraryhub.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "booksStock")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String author;
    private String publisher;
    private Integer quantity;
    private Integer available;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = true)
    private PurchaseBook purchaseBook;

    @OneToMany(mappedBy = "book" , cascade = CascadeType.ALL)
    private List<BookIssue> bookIssueList;
}
