package com.libraryhub.controller;

import com.libraryhub.model.BookIssue;
import com.libraryhub.model.BookIssueStatus;
import com.libraryhub.model.User;
import com.libraryhub.service.BookIssueServiceImpl;
import com.libraryhub.service.UserServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AllotmentHistoryController {

    private final UserServiceImpl userService;
    private final BookIssueServiceImpl bookIssueService;

    public AllotmentHistoryController(UserServiceImpl userService, BookIssueServiceImpl bookIssueService) {
        this.userService = userService;
        this.bookIssueService = bookIssueService;
    }

    /*
   Disclosure: Parts of this code were written with the help of AI-generated references
        and later reviewed, modified, and integrated by the developer.
    */
    @GetMapping("/allotment-history")
    public String viewHistory(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", required = false) BookIssueStatus status, Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username);

        if (user == null) return "redirect:/";

        List<BookIssue> historyRecords = bookIssueService.getAllHistory(keyword, status);
        LocalDate today = LocalDate.now();

        List<Map<String, Object>> recordsWithStatus = historyRecords.stream().map(record -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", record.getId());
            map.put("student", record.getStudent());
            map.put("book", record.getBook());
            map.put("issueDate", record.getIssueDate());
            map.put("returnDate", record.getReturnDate());

            String calculatedStatus;
            String actionButton = null;

            if (record.getStatus() == null) {
                calculatedStatus = "NA";
            }
            else if (record.getStatus() == BookIssueStatus.RETURNED) {
                calculatedStatus = "Returned";
            }
            else if (record.getStatus() == BookIssueStatus.LATE_RETURN) {
                calculatedStatus = "Late Return";
            }
            else if (record.getStatus() == BookIssueStatus.ISSUED) {
                if (record.getReturnDate() != null && today.isAfter(record.getReturnDate())) {
                    calculatedStatus = "Overtime";
                    actionButton = "LateReturn";
                } else {
                    calculatedStatus = "Issued";
                    actionButton = "Return";
                }
            }
            else if (record.getStatus() == BookIssueStatus.OVERDUE) {
                calculatedStatus = "Overtime";
                actionButton = "LateReturn";
            }
            else {
                calculatedStatus = record.getStatus().name();
            }

            map.put("calculatedStatus", calculatedStatus);
            map.put("actionButton", actionButton);

            return map;
        }).toList();

        model.addAttribute("historyRecords", recordsWithStatus);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status != null ? status.name() : "");
        model.addAttribute("allStatuses", BookIssueStatus.values());

        return "allotment-history";
    }

    @PostMapping("/update-status")
    public String updateStatus(@RequestParam("issueId") Long issueId, @RequestParam("type") String type, RedirectAttributes redirectAttributes) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username);

        if (user == null) {
            return "redirect:/";
        }

        BookIssue issue = bookIssueService.findById(Math.toIntExact(issueId));

        if (issue != null) {
            if ("Return".equals(type)) {
                issue.setStatus(BookIssueStatus.RETURNED);
            }
            else if ("LateReturn".equals(type)) {
                issue.setStatus(BookIssueStatus.LATE_RETURN);
            }
            bookIssueService.saveOrUpdate(issue);
            redirectAttributes.addFlashAttribute("successMessage", "Book status updated successfully!");
        }
        else {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update book status.");
        }
        return "redirect:/allotment-history";
    }
}
