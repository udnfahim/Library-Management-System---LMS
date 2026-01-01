package com.libraryhub.repository;

import com.libraryhub.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Repository
public interface BookRepository extends JpaRepository<Book,Integer> {
    List<Book> findAllByPurchaseBookId(Integer purchaseBookId);
}
