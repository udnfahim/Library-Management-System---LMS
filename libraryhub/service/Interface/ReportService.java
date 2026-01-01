package com.libraryhub.service.Interface;

import com.libraryhub.model.BookIssue;
import com.libraryhub.model.PurchaseBook;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {

    /*
   Disclosure: Parts of this code were written with the help of AI-generated references
        and later reviewed, modified, and integrated by the developer.
    */

    long totalBooks();
    long totalPurchasedBooks();
    double totalPurchaseValue();

    long totalAllotted();
    long totalReturned();
    long totalLateReturned();

    long totalStudents();
    long totalAdmins();
    long totalPublishers();
    long totalVendors();

    List<BookIssue> bookAllotments(LocalDate start, LocalDate end);
    List<PurchaseBook> purchases(LocalDate start, LocalDate end);
    List<BookIssue> submissions(LocalDate start, LocalDate end);
}
