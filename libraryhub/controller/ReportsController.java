package com.libraryhub.controller;

import com.libraryhub.model.BookIssue;
import com.libraryhub.model.BookIssueStatus;
import com.libraryhub.model.PurchaseBook;
import com.libraryhub.model.User;
import com.libraryhub.service.ReportServiceImpl;
import com.libraryhub.service.UserServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class ReportsController {

    private final UserServiceImpl userService;
    private final ReportServiceImpl reportService;

    public ReportsController(UserServiceImpl userService, ReportServiceImpl reportService) {
        this.userService = userService;
        this.reportService = reportService;
    }

    /*
   Disclosure: Parts of this code were written with the help of AI-generated references
        and later reviewed, modified, and integrated by the developer.
    */
    @GetMapping("/reports")
    public String reports(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username);

        if (user == null) {
            return "redirect:/";
        }

        model.addAttribute("username", username);

        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        model.addAttribute("totalBooks", reportService.totalBooks());
        model.addAttribute("totalPurchasedBooks", reportService.totalPurchasedBooks());
        model.addAttribute("totalPurchaseValue", reportService.totalPurchaseValue());
        model.addAttribute("totalAllotted", reportService.totalAllotted());
        model.addAttribute("totalReturned", reportService.totalReturned());
        model.addAttribute("totalLateReturned", reportService.totalLateReturned());
        model.addAttribute("totalStudents", reportService.totalStudents());
        model.addAttribute("totalAdmins", reportService.totalAdmins());
        model.addAttribute("totalPublishers", reportService.totalPublishers());
        model.addAttribute("totalVendors", reportService.totalVendors());

        model.addAttribute("bookAllotments",
                reportService.bookAllotments(startDate, endDate));
        model.addAttribute("purchases",
                reportService.purchases(startDate, endDate));
        model.addAttribute("submissions",
                reportService.submissions(startDate, endDate));

        List<BookIssue> submissions = reportService.submissions(startDate, endDate);

        List<String> chartLabels = submissions.stream()
                .map(s -> s.getStudent() != null ? s.getStudent().getName() : "Unknown")
                .distinct()
                .toList();

        List<Long> chartOnTime = chartLabels.stream()
                .map(name -> submissions.stream()
                        .filter(s -> s.getStudent() != null)
                        .filter(s -> name.equals(s.getStudent().getName()))
                        .filter(s -> s.getStatus() == BookIssueStatus.RETURNED)
                        .count())
                .toList();

        List<Long> chartLate = chartLabels.stream()
                .map(name -> submissions.stream()
                        .filter(s -> s.getStudent() != null)
                        .filter(s -> name.equals(s.getStudent().getName()))
                        .filter(s -> s.getStatus() == BookIssueStatus.LATE_RETURN)
                        .count())
                .toList();

        model.addAttribute("chartLabels", chartLabels);
        model.addAttribute("chartOnTime", chartOnTime);
        model.addAttribute("chartLate", chartLate);

        Map<String, Double> purchaseMap = reportService.purchases(startDate, endDate).stream()
                .collect(Collectors.groupingBy(
                        p -> p.getName(),
                        Collectors.summingDouble(p -> p.getPricePerBook() * p.getQuantity())));

        List<String> purchaseLabels = new ArrayList<>(purchaseMap.keySet());
        List<Double> purchaseData = purchaseLabels.stream()
                .map(purchaseMap::get)
                .toList();

        model.addAttribute("purchaseChartLabels", purchaseLabels);
        model.addAttribute("purchaseChartData", purchaseData);

        return "reports";
    }
}
