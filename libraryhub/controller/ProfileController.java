package com.libraryhub.controller;
import com.libraryhub.dto.UserDto;
import com.libraryhub.dto.UserPassResetDto;
import com.libraryhub.model.User;
import com.libraryhub.service.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
public class ProfileController {

    private final UserServiceImpl userService;

    public ProfileController(UserServiceImpl userService){
        this.userService=userService;
    }

    @GetMapping("/profile")
    public String getProfile(Model model){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username);

        if(user == null){
            return "redirect:/";
        }
        model.addAttribute("user", user);
        model.addAttribute("passResetDto", new UserPassResetDto("","",""));
        return "profile";
    }

    @PostMapping("/profile/change-password")
    public String changePassword(@ModelAttribute("passResetDto") UserPassResetDto dto,RedirectAttributes redirectAttributes) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username);

        if (user == null) {
            return "redirect:/";
        }
        String error = userService.passwordReset(dto, user);
        if (error != null) {
            redirectAttributes.addFlashAttribute("errorPassword", error);
        }
        else {
            redirectAttributes.addFlashAttribute("successPassword", "Password updated successfully!");
        }
        return "redirect:/profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute("user") User updatedUser, RedirectAttributes redirectAttributes) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username);

        if (user == null) {
            return "redirect:/";
        }

        updatedUser.setPhotoUrl(user.getPhotoUrl());
        userService.updateProfile(user.getId(), updatedUser);

        redirectAttributes.addFlashAttribute("successProfile", "Profile updated!");

        return "redirect:/profile";
    }

    /*
   Disclosure: Parts of this code were written with the help of AI-generated references
        and later reviewed, modified, and integrated by the developer.
    */
    @PostMapping("/profile/delete")
    public String deleteProfile(HttpServletRequest request, HttpServletResponse response) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken)) {

            String username = auth.getName();
            User user = userService.findByUsername(username);

            if (user != null) {
                userService.deleteUserById(user.getId());
            }
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/";
    }

    /*
       Disclosure: Parts of this code were written with the help of AI-generated references
            and later reviewed, modified, and integrated by the developer.
        */
    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/";
    }

}
