package com.libraryhub.repository;

import com.libraryhub.model.PurchaseBook;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseBookRepository extends JpaRepository<PurchaseBook,Integer> {

    List<PurchaseBook> findByPublicationsId(int publicationsId);
    Optional<PurchaseBook> findByInvoice(String invoice);

    /*
   Disclosure: Parts of this code were written with the help of AI-generated references
        and later reviewed, modified, and integrated by the developer.
    */
    List<PurchaseBook> findByNameContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrInvoiceContainingIgnoreCase(String name, String author, String invoice);
}
