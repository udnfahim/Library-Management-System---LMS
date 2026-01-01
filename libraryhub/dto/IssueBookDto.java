package com.libraryhub.dto;

import java.time.LocalDate;

public record IssueBookDto( Integer bookId , String studentId , LocalDate issueDate , LocalDate returnDate) {
}
