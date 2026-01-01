package com.libraryhub.service.Interface;

import com.libraryhub.model.Book;

import java.util.List;

public interface BookService {
    List<Book> findAllBooks();
    Book findBookById(Integer id);
    Book saveBook(Book book);
    boolean deleteBookById(Integer id);
    public List<Book> searchBooks(String keyword);
    List<Book> findAllBooksByPurchase(Integer purchaseBookId);
}