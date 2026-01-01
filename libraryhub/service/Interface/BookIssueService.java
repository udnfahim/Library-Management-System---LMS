package com.libraryhub.service.Interface;

import com.libraryhub.dto.IssueBookDto;
import com.libraryhub.model.BookIssue;
import com.libraryhub.model.BookIssueStatus;

import java.util.List;

public interface BookIssueService {
    void issueBook(IssueBookDto dto);
    List<BookIssue> getAllIssues();
    List<BookIssue> getIssuesByStudentId(String studentId);
    List<BookIssue> getIssuesByBookName(String keyword);
    public List<BookIssue> searchActiveIssues(String keyword);
    public BookIssue findById(int issueId);
    public BookIssue saveOrUpdate(BookIssue issue);
    public List<BookIssue> getAllHistory(String keyword, BookIssueStatus status);
}
