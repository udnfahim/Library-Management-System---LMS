package com.libraryhub.controller;
import com.libraryhub.dto.UserDto;
import com.libraryhub.model.User;
import com.libraryhub.service.UserServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UserServiceImpl userService;
    public AuthController(UserServiceImpl userService){
        this.userService=userService;
    }

    @GetMapping("/registration")
    public String getRegistration(Model model){
        model.addAttribute("userDto" , new UserDto("","","","","",""));
        return "registration";
    }

    @PostMapping("/registration")
    public String  postRegistration(@ModelAttribute("userDto")UserDto userDto, Model model){

        String  user = userService.register(userDto);

        if(user != null){
            model.addAttribute("error",user);
            return "registration";
        }
        return "redirect:/";
    }

    @GetMapping("/")
    public String getLogin(Model model){
        model.addAttribute("dto",new UserDto("","","","","",""));
        return "login";
    }

    @GetMapping("/forgot-password")
    public String getForgotPasswordPage(Model model) {
        model.addAttribute("dto", new UserDto("", "", "", "", "", ""));
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String postForgotPassword(@ModelAttribute("dto") UserDto dto, Model model) {

        if (dto.password() == null || dto.password().isEmpty()) {

            String error = userService.emailValidation(dto.email());

            if (error != null) {
                model.addAttribute("error", error);
                model.addAttribute("showReset", false);
            }
            else {
                model.addAttribute("showReset", true);
            }

            model.addAttribute("dto", dto);

            return "forgot-password";
        }
        else {

            String error = userService.resetPasswordByEmail(dto.email(), dto.password(), dto.confirmPassword());

            if (error != null) {
                model.addAttribute("error", error);
                model.addAttribute("showReset", true);
            }
            else {
                model.addAttribute("success", "Password updated successfully. You can now login.");
                model.addAttribute("showReset", false);
            }

            model.addAttribute("dto", dto);
            return "forgot-password";
        }
    }
}
