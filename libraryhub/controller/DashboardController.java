package com.libraryhub.controller;

import com.libraryhub.model.User;
import com.libraryhub.service.ReportServiceImpl;
import com.libraryhub.service.UserServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final UserServiceImpl userService;
    private final ReportServiceImpl reportService;

    public DashboardController(UserServiceImpl userService, ReportServiceImpl reportService) {
        this.userService = userService;
        this.reportService = reportService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username);

        model.addAttribute("username", username);
        model.addAttribute("name",user.getName());

        long totalBooks = reportService.totalBooks();
        long availableBooks = totalBooks - reportService.totalAllotted();
        long booksIssued = reportService.totalAllotted();
        long overdueBooks = reportService.totalLateReturned();
        long totalStudents = reportService.totalStudents();
        long totalVendors = reportService.totalVendors();
        long totalPublications = reportService.totalPublishers();

        model.addAttribute("totalBooks", totalBooks);
        model.addAttribute("availableBooks", availableBooks);
        model.addAttribute("booksIssued", booksIssued);
        model.addAttribute("overdueBooks", overdueBooks);
        model.addAttribute("totalStudents", totalStudents);
        model.addAttribute("totalVendors", totalVendors);
        model.addAttribute("totalPublications", totalPublications);

        model.addAttribute("dailyQuote", "“Reading is to the mind what exercise is to the body.”");

        return "dashboard";
    }
}
