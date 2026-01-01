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
@Table(name = "publications")
public class Publications {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;
    @Column(nullable = true,length = 30)
    private String name;
    @Column(nullable = true , length = 50)
    private String address;
    @Column(length = 40, nullable = true)
    private String description;

    @OneToMany(mappedBy = "publications" , fetch = FetchType.LAZY , orphanRemoval = true)
    private List<PurchaseBook> purchaseBookList;
}
