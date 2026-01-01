package com.libraryhub.controller;
import com.libraryhub.dto.BookDto;
import com.libraryhub.model.Book;
import com.libraryhub.model.PurchaseBook;
import com.libraryhub.model.User;
import com.libraryhub.service.BookIssueServiceImpl;
import com.libraryhub.service.BookServiceImpl;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Controller
public class BooksManagementController {
    private final UserServiceImpl userService;
    private final BookServiceImpl bookService;
    private final PurchaseBookServiceImpl purchaseBookService;

    public BooksManagementController(UserServiceImpl userService, BookServiceImpl bookService, PurchaseBookServiceImpl purchaseBookService) {
        this.userService = userService;
        this.bookService = bookService;
        this.purchaseBookService = purchaseBookService;
    }

    @GetMapping("/books-management")
    public String getBooksPage(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username);

        if (user == null) {
            return "redirect:/";
        }

        List<Book> books = bookService.findAllBooks();
        List<PurchaseBook> purchaseBooks = purchaseBookService.findAllPurchaseBook();

        model.addAttribute("username", user.getUsername());
        model.addAttribute("books", books);
        model.addAttribute("purchaseBooks", purchaseBooks);
        model.addAttribute("dto", new BookDto(null, null, "", "", "",  null));

        return "books-management";
    }

    /*
   Disclosure: Parts of this code were written with the help of AI-generated references
        and later reviewed, modified, and integrated by the developer.
    */
    @PostMapping("/books-management")
    public String saveBook(@ModelAttribute("dto") BookDto bookDto, RedirectAttributes redirectAttributes) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username);

        if (user == null) {
            return "redirect:/";
        }

        if (bookDto.purchaseId() == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Purchase book must be selected.");
            return "redirect:/books-management";
        }

        PurchaseBook pb = purchaseBookService.findByIdPurchaseBook(bookDto.purchaseId());
        if (pb == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Selected purchased book not found.");
            return "redirect:/books-management";
        }

        List<Book> linkedBooks = bookService.findAllBooksByPurchase(pb.getId());
        int totalInLibrary = linkedBooks.stream().mapToInt(Book::getQuantity).sum();

        if (bookDto.id() == null || bookDto.id() == 0) {
            if (bookDto.quantity() + totalInLibrary > pb.getQuantity()) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Cannot add " + bookDto.quantity() + " books for '"
                                + pb.getName() + "'. Only "
                                + (pb.getQuantity() - totalInLibrary)
                                + " left in purchased stock.");

                return "redirect:/books-management";
            }

            Book newBook = new Book();
            newBook.setName(pb.getName());
            newBook.setAuthor(pb.getAuthor());
            newBook.setPublisher(pb.getPublications().getName());
            newBook.setQuantity(bookDto.quantity());
            newBook.setAvailable(bookDto.quantity());
            newBook.setPurchaseBook(pb);
            bookService.saveBook(newBook);

            redirectAttributes.addFlashAttribute("successMessage", "Book added successfully from purchased stock.");

        } else {
            Book existing = bookService.findBookById(bookDto.id());
            if (existing == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Book not found for update.");
                return "redirect:/books-management";
            }

            int issuedBooks = existing.getQuantity() - existing.getAvailable();
            int otherBooksQuantity = totalInLibrary - existing.getQuantity();

            if (bookDto.quantity() + otherBooksQuantity < issuedBooks) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Cannot set quantity to " + bookDto.quantity() +
                                ". " + issuedBooks + " copies are currently issued.");
                return "redirect:/books-management";
            }

            int maxAllowed = pb.getQuantity() - otherBooksQuantity;
            if (bookDto.quantity() > maxAllowed) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Cannot set quantity to " + bookDto.quantity() + " for '" + pb.getName() +
                                "'. Only " + maxAllowed + " left in purchased stock.");
                return "redirect:/books-management";
            }

            existing.setName(bookDto.name());
            existing.setAuthor(bookDto.author());
            existing.setPublisher(bookDto.publisher());
            existing.setQuantity(bookDto.quantity());
            existing.setAvailable(bookDto.quantity() - issuedBooks);
            bookService.saveBook(existing);

            redirectAttributes.addFlashAttribute("successMessage", "Book updated successfully.");
        }

        return "redirect:/books-management";
    }

    @PostMapping("/books-management/delete")
    public String deleteBook(@ModelAttribute("dto") BookDto bookDto, RedirectAttributes redirectAttributes) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username);

        if (user == null) {
            return "redirect:/";
        }
        Book book = bookService.findBookById(bookDto.id());
        if (book == null) {
            redirectAttributes.addFlashAttribute("error", "Book not found.");
        }
        else if (book.getPurchaseBook() != null) {
            redirectAttributes.addFlashAttribute("error", "Cannot delete this book because it is linked to a purchased book.");
        }
        else {
            boolean deleted = bookService.deleteBookById(bookDto.id());
            if (deleted) {
                redirectAttributes.addFlashAttribute("successMessage", "Book deleted successfully.");
            }
            else {
                redirectAttributes.addFlashAttribute("error", "Failed to delete book.");
            }
        }
        return "redirect:/books-management";
    }
}
