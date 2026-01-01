package com.libraryhub.service;

import com.libraryhub.model.*;
import com.libraryhub.repository.*;
import com.libraryhub.service.Interface.ReportService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    private final BookRepository bookRepository;
    private final PurchaseBookRepository purchaseBookRepository;
    private final BookIssueRepository bookIssueRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final PublicationsRepository publicationsRepository;
    private final VendorRepository vendorRepository;

    public ReportServiceImpl(
            BookRepository bookRepository,
            PurchaseBookRepository purchaseBookRepository,
            BookIssueRepository bookIssueRepository,
            StudentRepository studentRepository,
            UserRepository userRepository,
            PublicationsRepository publicationsRepository,
            VendorRepository vendorRepository
    ) {
        this.bookRepository = bookRepository;
        this.purchaseBookRepository = purchaseBookRepository;
        this.bookIssueRepository = bookIssueRepository;
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.publicationsRepository = publicationsRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public long totalBooks() {
        return bookRepository.count();
    }

    @Override
    public long totalPurchasedBooks() {
        return purchaseBookRepository.count();
    }

    @Override
    public double totalPurchaseValue() {
        return purchaseBookRepository.findAll()
                .stream()
                .mapToDouble(p -> p.getPricePerBook() * p.getQuantity())
                .sum();
    }

    @Override
    public long totalAllotted() {
        return bookIssueRepository.findByStatus(BookIssueStatus.ISSUED).size();
    }

    @Override
    public long totalReturned() {
        return bookIssueRepository.findByStatus(BookIssueStatus.RETURNED).size();
    }

    @Override
    public long totalLateReturned() {
        return bookIssueRepository.findByStatus(BookIssueStatus.LATE_RETURN).size();
    }

    @Override
    public long totalStudents() {
        return studentRepository.count();
    }

    @Override
    public long totalAdmins() {
        return userRepository.count();
    }

    @Override
    public long totalPublishers() {
        return publicationsRepository.count();
    }

    @Override
    public long totalVendors() {
        return vendorRepository.count();
    }



    /*
   Disclosure: Parts of this code were written with the help of AI-generated references
        and later reviewed, modified, and integrated by the developer.
    */
    @Override
    public List<BookIssue> bookAllotments(LocalDate start, LocalDate end) {
        return bookIssueRepository.findAll()
                .stream()
                .filter(b -> isBetween(b.getIssueDate(), start, end))
                .toList();
    }

    @Override
    public List<PurchaseBook> purchases(LocalDate start, LocalDate end) {
        return purchaseBookRepository.findAll()
                .stream()
                .filter(p -> isBetween(p.getPurchaseDate(), start, end))
                .toList();
    }

    /*
   Disclosure: Parts of this code were written with the help of AI-generated references
        and later reviewed, modified, and integrated by the developer.
    */
    @Override
    public List<BookIssue> submissions(LocalDate start, LocalDate end) {
        return bookIssueRepository.findAll()
                .stream()
                .filter(b -> b.getStatus() == BookIssueStatus.RETURNED || b.getStatus() == BookIssueStatus.LATE_RETURN)
                .filter(b -> b.getReturnDate() != null && isBetween(b.getReturnDate(), start, end))
                .toList();
    }
    private boolean isBetween(LocalDate date, LocalDate start, LocalDate end) {
        if (date == null) return false;
        if (start == null && end == null) return true;
        if (start == null) return !date.isAfter(end);
        if (end == null) return !date.isBefore(start);
        return !date.isBefore(start) && !date.isAfter(end);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
}
