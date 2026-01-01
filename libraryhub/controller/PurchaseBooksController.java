package com.libraryhub.controller;
import com.libraryhub.dto.PurchaseBookDto;
import com.libraryhub.model.Publications;
import com.libraryhub.model.PurchaseBook;
import com.libraryhub.model.User;
import com.libraryhub.service.PublicationsServiceImpl;
import com.libraryhub.service.PurchaseBookServiceImpl;
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
public class PurchaseBooksController {
    private final UserServiceImpl userService;
    private final PurchaseBookServiceImpl purchaseBookService;
    private final PublicationsServiceImpl publicationsService;

    public PurchaseBooksController(UserServiceImpl userService, PurchaseBookServiceImpl purchaseBookService,
                                   PublicationsServiceImpl publicationsService) {
        this.userService = userService;
        this.purchaseBookService = purchaseBookService;
        this.publicationsService = publicationsService;
    }

    @GetMapping("/purchase-books")
    public String getPurchaseBooksPage(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username);

        if (user == null) {
            return "redirect:/";
        }

        List<PurchaseBook> purchaseBooks = purchaseBookService.findAllPurchaseBook();
        List<Publications> publicationsList = publicationsService.findAllPublications();
        model.addAttribute("username", user.getUsername());
        model.addAttribute("purchaseBooks", purchaseBooks);
        model.addAttribute("publicationsList", publicationsList);
        model.addAttribute("dto", new PurchaseBookDto(null, "", "" ,0, 0.0, null, "",null));

        return "purchase-books";
    }

    @PostMapping("/purchase-books")
    public String saveOrUpdatePurchaseBook(@ModelAttribute("dto") PurchaseBookDto dto, RedirectAttributes redirectAttributes) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username);

        if (user == null) {
            return "redirect:/";
        }

        Publications pub = null;
        if (dto.publicationId() != null) {
            pub = publicationsService.findByIdPublications(dto.publicationId().intValue());
        }
        PurchaseBook existingInvoice = purchaseBookService.findByInvoice(dto.invoice());
        if (existingInvoice != null) {
            if (dto.id() == null || existingInvoice.getId() != dto.id()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Invoice already exists.");
                return "redirect:/purchase-books";
            }
        }

        if (dto.id() == null) {
            PurchaseBook newBook = dto.toEntity(pub);
            purchaseBookService.savePurchaseBook(newBook);
            redirectAttributes.addFlashAttribute("successMessage", "Book purchased successfully.");
        } else {
            // Updating existing purchase
            PurchaseBook existing = purchaseBookService.findByIdPurchaseBook(dto.id());
            if (existing != null) {
                existing.setName(dto.name());
                existing.setAuthor(dto.author());
                existing.setQuantity(dto.quantity());
                existing.setPricePerBook(dto.pricePerBook());
                existing.setPurchaseDate(dto.purchaseDate());
                existing.setInvoice(dto.invoice());
                existing.setPublications(pub);
                purchaseBookService.updatePurchaseBook(existing);
                redirectAttributes.addFlashAttribute("successMessage", "Purchase updated successfully.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Purchase record not found.");
            }
        }

        return "redirect:/purchase-books";
    }

    @GetMapping("/purchase-books-filter")
    public String getPurchaseBooks(@RequestParam(value = "keyword", required = false) String keyword, Model model, HttpSession session) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username);

        if (user == null) return "redirect:/";

        List<PurchaseBook> purchaseBooks;
        if (keyword != null && !keyword.isEmpty()) {
            purchaseBooks = purchaseBookService.searchByKeyword(keyword);
        } else {
            purchaseBooks = purchaseBookService.findAllPurchaseBook();
        }

        model.addAttribute("username", user.getUsername());
        model.addAttribute("purchaseBooks", purchaseBooks);
        model.addAttribute("publicationsList", publicationsService.findAllPublications());
        model.addAttribute("dto", new PurchaseBookDto(null, "", "", 0, 0.0, null, "", null));
        return "purchase-books";
    }

    //fk erro so commented
//    @PostMapping("/purchase-books/delete")
//    public String deletePurchaseBook(@RequestParam("id") Integer id, RedirectAttributes redirectAttributes) {

//    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//    String username = auth.getName();
//    User user = userService.findByUsername(username);
//
//            if (user == null) {
//            return "redirect:/";
//               }
//
//        boolean deleted = purchaseBookService.deleteByIdPurchaseBook(id);
//        if (deleted) {
//            redirectAttributes.addFlashAttribute("successMessage", "Purchased book deleted successfully.");
//        }
//        else {
//            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete purchased book.");
//        }
//
//        return "redirect:/purchase-books";
//    }

}
