package com.libraryhub.controller;

import com.libraryhub.dto.VendorDto;
import com.libraryhub.model.User;
import com.libraryhub.model.Vendor;
import com.libraryhub.service.UserServiceImpl;
import com.libraryhub.service.VendorServiceImpl;
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

@Controller
public class VendorManagementController {
    private final UserServiceImpl userService;
    private final VendorServiceImpl vendorService;
    public VendorManagementController(UserServiceImpl userService, VendorServiceImpl vendorService){
        this.userService = userService;
        this.vendorService = vendorService;
    }
    @GetMapping("/vendor-management")
    public String getVendorPage(Model model){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username);

        if (user == null) {
            return "redirect:/";
        }

        model.addAttribute("username", user.getUsername());
        model.addAttribute("vendors", vendorService.findAllVendor());
        model.addAttribute("dto", new VendorDto(null, "", "", null, ""));

        return "vendor-management";
    }

    @PostMapping("/vendor-management")
    public String saveVendor(@ModelAttribute("dto") VendorDto vendorDto, RedirectAttributes redirectAttributes) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username);

        if (user == null) {
            return "redirect:/";
        }

        if (vendorDto.id() == null) {
            vendorService.saveVendor(vendorDto.toEntity());
            redirectAttributes.addFlashAttribute("successMessage", "Vendor added successfully.");
        }
        else {
            Vendor existing = vendorService.findById(vendorDto.id());
            if (existing != null) {
                existing.setName(vendorDto.name());
                existing.setCompany(vendorDto.company());
                existing.setDate(vendorDto.date());
                existing.setNumber(vendorDto.number());
                vendorService.updateVendor(existing);
                redirectAttributes.addFlashAttribute("successMessage", "Vendor updated successfully.");
            }
            else {
                redirectAttributes.addFlashAttribute("errorMessage", "Vendor not found.");
            }
        }
        return "redirect:/vendor-management";
    }

    @PostMapping("/vendor-management/delete")
    public String deleteVendor(@RequestParam("id") Integer id, RedirectAttributes redirectAttributes) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username);

        if (user == null) {
            return "redirect:/";
        }

        if (vendorService.deleteVendor(id)) {
            redirectAttributes.addFlashAttribute("successMessage", "Vendor deleted successfully.");
        }
        else {
            redirectAttributes.addFlashAttribute("errorMessage", "Vendor is not Found!");
        }

        return "redirect:/vendor-management";
    }

    @GetMapping("/vendor-management/filter")
    public String filterVendors(@RequestParam("keyword") String keyword, Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username);

        if (user == null) {
            return "redirect:/";
        }

        model.addAttribute("username", user.getUsername());
        model.addAttribute("vendors", vendorService.findByKeyword(keyword));
        model.addAttribute("keyword", keyword);
        model.addAttribute("dto", new VendorDto(null, "", "", null, ""));

        return "vendor-management";
    }
}
