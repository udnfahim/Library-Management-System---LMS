package com.libraryhub.service.Interface;

import com.libraryhub.model.PurchaseBook;

import java.util.List;

public interface PurchaseBookService {
    PurchaseBook savePurchaseBook(PurchaseBook purchaseBook);
    PurchaseBook updatePurchaseBook(PurchaseBook purchaseBook);
    List<PurchaseBook> findAllPurchaseBook();
    PurchaseBook findByIdPurchaseBook(int id);
    boolean deleteByIdPurchaseBook(int id);
    public boolean existsByPublicationId(int id);
    public List<PurchaseBook> searchByKeyword(String keyword);
    PurchaseBook findByInvoice(String invoice);
}
