package com.libraryhub.dto;

import com.libraryhub.model.Book;
import com.libraryhub.model.PurchaseBook;

public record BookDto(Integer id,Integer purchaseId, String name, String author, String publisher, Integer quantity) {

    public Book toEntity(PurchaseBook purchaseBook) {
        Book book = new Book();
        book.setName(name);
        book.setAuthor(author);
        book.setPublisher(publisher);
        book.setQuantity(quantity);
        book.setAvailable(quantity);
        book.setPurchaseBook(purchaseBook);
        return book;
    }
}
