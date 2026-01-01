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
@Table(name = "purchaseBook")
public class PurchaseBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(length = 60)
    private String name;
    @Column(length = 30)
    private String author;
    private int quantity;
    private double pricePerBook;
    @Column(nullable = true)
    private LocalDate purchaseDate;
    @Column(unique = true,nullable = false)
    private String invoice;

    @ManyToOne(fetch = FetchType.EAGER)
    private Publications publications;

    public PurchaseBook(int id, String name, String author, Long publicationId, int quantity, double pricePerBook, LocalDate purchaseDate, String invoice) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.quantity = quantity;
        this.pricePerBook = pricePerBook;
        this.purchaseDate = purchaseDate;
        this.invoice = invoice;
    }

    @OneToMany(mappedBy = "purchaseBook", fetch = FetchType.LAZY)
    private List<Book> bookList;
}
