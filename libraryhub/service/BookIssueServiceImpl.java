package com.libraryhub.service;

import com.libraryhub.dto.IssueBookDto;
import com.libraryhub.model.Book;
import com.libraryhub.model.BookIssue;
import com.libraryhub.model.BookIssueStatus;
import com.libraryhub.model.Student;
import com.libraryhub.repository.BookIssueRepository;
import com.libraryhub.repository.BookRepository;
import com.libraryhub.repository.StudentRepository;
import com.libraryhub.service.Interface.BookIssueService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookIssueServiceImpl implements BookIssueService {

    private final BookIssueRepository bookIssueRepository;
    private final BookRepository bookRepository;
    private final StudentRepository studentRepository;

    public BookIssueServiceImpl(BookIssueRepository bookIssueRepository, BookRepository bookRepository, StudentRepository studentRepository) {
        this.bookIssueRepository = bookIssueRepository;
        this.bookRepository = bookRepository;
        this.studentRepository = studentRepository;
    }


    /*
   Disclosure: Parts of this code were written with the help of AI-generated references
        and later reviewed, modified, and integrated by the developer.
    */
    @Override
    public void issueBook(IssueBookDto dto) {

        Book book = bookRepository.findById(dto.bookId()).orElseThrow(() -> new RuntimeException("Book not found"));

        if (book.getQuantity() <= 0) {
            throw new RuntimeException("Book out of stock");
        }

        Student student = studentRepository.findByStudentId(dto.studentId()).orElseThrow(() -> new RuntimeException("Student not found"));

        if (student.getEmail() == null || student.getName() == null) {
            throw new RuntimeException("Student email or name missing");
        }

        BookIssue issue = new BookIssue();
        issue.setBook(book);
        issue.setStudent(student);
        issue.setIssueDate(dto.issueDate());
        issue.setStatus(BookIssueStatus.ISSUED);
        issue.setReturnDate(dto.returnDate());

        bookIssueRepository.save(issue);
        book.setQuantity(book.getQuantity() - 1);
    }


    @Override
    public List<BookIssue> getAllIssues() {
        return bookIssueRepository.findAll();
    }

    @Override
    public List<BookIssue> getIssuesByStudentId(String studentId) {
        return bookIssueRepository.findByStudent_StudentId(studentId);
    }

    @Override
    public List<BookIssue> getIssuesByBookName(String keyword) {
        return bookIssueRepository.findByBook_NameContainingIgnoreCase(keyword);
    }

    @Override
    public List<BookIssue> getAllHistory(String keyword, BookIssueStatus status) {
        if ((keyword == null || keyword.isEmpty()) && status == null) {
            return bookIssueRepository.findAll();
        }
        else if ((keyword == null || keyword.isEmpty()) && status != null) {
            return bookIssueRepository.findByStatus(status);
        }
        else if ((keyword != null && !keyword.isEmpty()) && status == null) {
            return bookIssueRepository.findByKeyword(keyword);
        }
        else {
            return bookIssueRepository.findByKeywordAndStatus(keyword, status);
        }
    }

    @Override
    public List<BookIssue> searchActiveIssues(String keyword) {
        BookIssueStatus status = BookIssueStatus.ISSUED;
        if (keyword == null || keyword.trim().isEmpty()) {
            return bookIssueRepository.findByStatus(status);
        } else {
            return bookIssueRepository.findByKeywordAndStatus(keyword, status);
        }
    }

    @Override
    public BookIssue findById(int issueId) {
        return bookIssueRepository.findById(issueId).orElse(null);
    }

    @Override
    public BookIssue saveOrUpdate(BookIssue issue) {
        return bookIssueRepository.save(issue);
    }
}
