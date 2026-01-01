package com.libraryhub.controller;
import com.libraryhub.dto.PublicationsDto;
import com.libraryhub.model.Publications;
import com.libraryhub.model.User;
import com.libraryhub.service.PublicationsServiceImpl;
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


@Controller
public class PublicationsController {

    private final UserServiceImpl userService;
    private final PublicationsServiceImpl publicationsService;

    public PublicationsController(UserServiceImpl userService, PublicationsServiceImpl publicationsService){
        this.userService = userService;
        this.publicationsService=publicationsService;
    }

    @GetMapping("/publications")
    public String getPublications(Model model){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username);

        if (user == null) {
            return "redirect:/";
        }

        model.addAttribute("username",user.getUsername());
        model.addAttribute("publications",publicationsService.findAllPublications());
        model.addAttribute("dto",new PublicationsDto(null,"","",""));

        return "/publications";
    }


    @PostMapping("/publications")
    public String postPublications(@ModelAttribute("dto")PublicationsDto publicationsDto , RedirectAttributes redirectAttributes ){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username);

        if (user == null) {
            return "redirect:/";
        }

        if (publicationsDto.id() == null) {
            publicationsService.addPublications(publicationsDto.toEntity());
            redirectAttributes.addFlashAttribute("successMessage", "Publication added successfully!");
        }
        else {

            Publications available = publicationsService.findByIdPublications(publicationsDto.id());

            if (available != null) {
                available.setName(publicationsDto.name());
                available.setAddress(publicationsDto.address());
                available.setDescription(publicationsDto.description());
                publicationsService.updatePublications(available);
                redirectAttributes.addFlashAttribute("successMessage", "Publication updated successfully!");
            }
            else {
                redirectAttributes.addFlashAttribute("errorMessage", "Publication not found for update.");
            }
        }
        return "redirect:/publications";
    }

    @PostMapping("/publications/delete")
    public String deletePublication(@RequestParam("id") Integer id, RedirectAttributes redirectAttributes) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username);

        if (user == null) {
            return "redirect:/";
        }

        if(publicationsService.deletePublications(id)){
            redirectAttributes.addFlashAttribute("successMessage", "Publication deleted successfully!");
        }
        else {
            redirectAttributes.addFlashAttribute("errorMessage", "Unable to delete. Please remove associated records before attempting to delete this publication!");
        }

        return "redirect:/publications";
    }
}
