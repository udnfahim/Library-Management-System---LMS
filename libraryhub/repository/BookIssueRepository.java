package com.libraryhub.repository;

import com.libraryhub.model.Book;
import com.libraryhub.model.BookIssue;
import com.libraryhub.model.BookIssueStatus;
import com.libraryhub.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookIssueRepository extends JpaRepository<BookIssue,Integer> {
    List<BookIssue> findByStudent_StudentId(String studentId);
    List<BookIssue> findByBook_NameContainingIgnoreCase(String keyword);


    /*
   Disclosure: Parts of this code were written with the help of AI-generated references
        and later reviewed, modified, and integrated by the developer.
    */
    @Query("SELECT b FROM BookIssue b WHERE " +
            "LOWER(b.student.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.student.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.book.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<BookIssue> findByKeyword(@Param("keyword") String keyword);

    @Query("SELECT b FROM BookIssue b WHERE (:status IS NULL OR b.status = :status)")
    List<BookIssue> findByStatus(@Param("status") BookIssueStatus status);

    @Query("SELECT b FROM BookIssue b WHERE " +
            "((:keyword IS NULL OR LOWER(b.student.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(b.student.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(b.book.name) LIKE LOWER(CONCAT('%', :keyword, '%')))) AND " +
            "(:status IS NULL OR b.status = :status)")
    List<BookIssue> findByKeywordAndStatus(@Param("keyword") String keyword, @Param("status") BookIssueStatus status);

}
