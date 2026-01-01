package com.libraryhub.controller;

import com.libraryhub.dto.IssueBookDto;
import com.libraryhub.model.Book;
import com.libraryhub.model.User;
import com.libraryhub.service.BookIssueServiceImpl;
import com.libraryhub.service.BookServiceImpl;
import com.libraryhub.service.UserServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
public class BookAllotmentController {

    private final UserServiceImpl userService;
    private final BookServiceImpl bookService;
    private final BookIssueServiceImpl bookIssueService;

    public BookAllotmentController(UserServiceImpl userService, BookServiceImpl bookService, BookIssueServiceImpl bookIssueService) {
        this.userService = userService;
        this.bookService = bookService;
        this.bookIssueService = bookIssueService;
    }
    @GetMapping("/books-allotment")
    public String bookAllotment(@RequestParam(value = "keyword", required = false) String keyword, Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username);

        if (user == null) return "redirect:/";

        List<Book> books = (keyword != null && !keyword.isEmpty()) ? bookService.searchBooks(keyword) : bookService.findAllBooks();

        model.addAttribute("books", books);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("keyword", keyword);

        LocalDate today = LocalDate.now();
        model.addAttribute("minIssueDate", today.minusDays(7));
        model.addAttribute("maxIssueDate", today);
        model.addAttribute("minReturnDate", today.plusDays(1));

        return "books-allotment";
    }

    @PostMapping("/books-allotment-issue")
    public String issueBook(
            @RequestParam("bookId") Integer bookId,
            @RequestParam("studentId") String studentId,
            @RequestParam("issueDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate issueDate,
            @RequestParam("returnDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnDate,
            RedirectAttributes redirectAttributes) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username);

        if (user == null) {
            return "redirect:/";
        }

        IssueBookDto dto = new IssueBookDto(bookId, studentId, issueDate, returnDate);

        try {
            bookIssueService.issueBook(dto);
            redirectAttributes.addFlashAttribute("successMessage", "Book issued successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/books-allotment";
    }
}
