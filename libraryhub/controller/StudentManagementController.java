package com.libraryhub.controller;
import com.libraryhub.dto.StudentDto;
import com.libraryhub.model.Student;
import com.libraryhub.model.User;
import com.libraryhub.service.Interface.StudentService;
import com.libraryhub.service.UserServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class StudentManagementController {

    private final UserServiceImpl userService;
    private final StudentService studentService;

    public StudentManagementController(UserServiceImpl userService, StudentService studentService) {
        this.userService = userService;
        this.studentService = studentService;
    }

    @GetMapping("/student-management")
    public String getStudentPage(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username);

        if (user == null) {
            return "redirect:/";
        }

        model.addAttribute("username", user.getUsername());
        model.addAttribute("students", studentService.findAllStudent());
        model.addAttribute("dto", new StudentDto(null, "", "", "", "", "", null));

        return "student-management";
    }

    @PostMapping("/student-management")
    public String saveStudent(@ModelAttribute("dto") StudentDto studentDto, RedirectAttributes redirectAttributes) {


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username);

        if (user == null) {
            return "redirect:/";
        }

        if (studentDto.id() == null) {
            studentService.saveStudent(studentDto.toSave());
            redirectAttributes.addFlashAttribute("successMessage", "Student added successfully.");
        }
        else {
            Student existing = studentService.findByIdStudent(studentDto.id());
            if (existing != null) {
                existing.setName(studentDto.name());
                existing.setEmail(studentDto.email());
                existing.setMobile(studentDto.mobile());
                existing.setStudentClass(studentDto.studentClass());
                existing.setRegistrationDate(studentDto.registrationDate());
                studentService.saveStudent(existing);
                redirectAttributes.addFlashAttribute("successMessage", "Student updated successfully.");
            }
            else {
                redirectAttributes.addFlashAttribute("errorMessage", "Student not found.");
            }
        }

        return "redirect:/student-management";
    }

    @PostMapping("/student-management/delete")
    public String deleteStudent(@RequestParam("id") Integer id, RedirectAttributes redirectAttributes) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username);

        if (user == null) {
            return "redirect:/";
        }

        studentService.deleteByIdStudent(id);
        redirectAttributes.addFlashAttribute("successMessage", "Student deleted successfully.");

        return "redirect:/student-management";

    }

    @GetMapping("/student-management/filter")
    public String filterStudents(@RequestParam("keyword") String keyword, Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username);

        if (user == null) {
            return "redirect:/";
        }

        List<Student> students;

        if (keyword == null || keyword.isBlank()) {
            students = studentService.findAllStudent();
        }
        else {
            students = studentService.searchStudent(keyword);
        }

        model.addAttribute("username", user.getUsername());
        model.addAttribute("students", students);
        model.addAttribute("dto", new StudentDto(null, "", "", "", "", "", null));
        model.addAttribute("keyword", keyword);

        return "student-management";
    }
}
