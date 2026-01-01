package com.libraryhub.service;

import com.libraryhub.model.Book;
import com.libraryhub.repository.BookRepository;
import com.libraryhub.service.Interface.BookService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    public BookServiceImpl(BookRepository bookRepository){

        this.bookRepository=bookRepository;
    }

    @Override
    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book findBookById(Integer id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Override
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public boolean deleteBookById(Integer id) {
        bookRepository.deleteById(id);
        return true;
    }

    /*
   Disclosure: Parts of this code were written with the help of AI-generated references
        and later reviewed, modified, and integrated by the developer.
    */
    @Override
    public List<Book> searchBooks(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return bookRepository.findAll().stream()
                .filter(book -> book.getName().toLowerCase().contains(lowerKeyword)
                        || book.getAuthor().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findAllBooksByPurchase(Integer purchaseBookId) {
        return bookRepository.findAllByPurchaseBookId(purchaseBookId);
    }
}
