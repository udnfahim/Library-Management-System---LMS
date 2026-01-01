package com.libraryhub.dto;

import com.libraryhub.model.Publications;
import com.libraryhub.model.PurchaseBook;

import java.time.LocalDate;

public record PurchaseBookDto(Integer id, String name, String author, int quantity, double pricePerBook, LocalDate purchaseDate, String invoice,   Integer publicationId) {
    public PurchaseBook toEntity(Publications publications) {
        PurchaseBook purchaseBook = new PurchaseBook();
        purchaseBook.setName(name);
        purchaseBook.setAuthor(author);
        purchaseBook.setQuantity(quantity);
        purchaseBook.setPricePerBook(pricePerBook);
        purchaseBook.setPurchaseDate(purchaseDate);
        purchaseBook.setInvoice(invoice);
        purchaseBook.setPublications(publications);
        return purchaseBook;
    }
}
