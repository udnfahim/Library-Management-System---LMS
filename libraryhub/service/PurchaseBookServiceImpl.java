package com.libraryhub.service;

import com.libraryhub.model.PurchaseBook;
import com.libraryhub.repository.PurchaseBookRepository;
import com.libraryhub.service.Interface.PurchaseBookService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PurchaseBookServiceImpl implements PurchaseBookService {

    private final PurchaseBookRepository purchaseBookRepository;

    public PurchaseBookServiceImpl(PurchaseBookRepository purchaseBookRepository){
        this.purchaseBookRepository=purchaseBookRepository;
    }
    @Override
    public PurchaseBook savePurchaseBook(PurchaseBook purchaseBook) {
        return purchaseBookRepository.save(purchaseBook);
    }

    @Override
    public PurchaseBook updatePurchaseBook(PurchaseBook purchaseBook) {
        return purchaseBookRepository.save(purchaseBook);
    }

    @Override
    public List<PurchaseBook> findAllPurchaseBook() {
        return purchaseBookRepository.findAll();
    }

    @Override
    public PurchaseBook findByIdPurchaseBook(int id) {
        return purchaseBookRepository.findById(id).orElse(null);
    }

    @Override
    public boolean deleteByIdPurchaseBook(int id) {
        purchaseBookRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean existsByPublicationId(int id){
        List<PurchaseBook> books = purchaseBookRepository.findByPublicationsId(id);
        return books != null && !books.isEmpty();
    }

    @Override
    public List<PurchaseBook> searchByKeyword(String keyword) {
        return purchaseBookRepository.findByNameContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrInvoiceContainingIgnoreCase(keyword, keyword,keyword);
    }

    @Override
    public PurchaseBook findByInvoice(String invoice) {
        return purchaseBookRepository.findByInvoice(invoice).orElse(null);
    }
}
