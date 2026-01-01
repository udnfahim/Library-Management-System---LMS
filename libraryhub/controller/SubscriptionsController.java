package com.libraryhub.controller;
import com.libraryhub.dto.SubscriptionsDto;
import com.libraryhub.model.Subscriptions;
import com.libraryhub.model.User;
import com.libraryhub.service.SubscriptionsServiceImpl;
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
public class SubscriptionsController {

    private final UserServiceImpl userService;
    private final SubscriptionsServiceImpl subscriptionsService;

    public SubscriptionsController(UserServiceImpl userService, SubscriptionsServiceImpl subscriptionsService){
        this.userService = userService;
        this.subscriptionsService = subscriptionsService;
    }

    @GetMapping("/subscriptions")
    public String getSubscriptions(Model model){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username);

        if(user == null){
            return "redirect:/";
        }

        model.addAttribute("username",user.getUsername());

        model.addAttribute("subscriptions",subscriptionsService.findAllSubscriptions());
        model.addAttribute("dto",new SubscriptionsDto(null,"",0,0,""));

        return "subscriptions";
    }

    @PostMapping("/subscriptions")
    public String postSubscriptions(@ModelAttribute("dto")SubscriptionsDto subscriptionsDto , RedirectAttributes redirectAttributes){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username);

        if (user == null) {
            return "redirect:/";
        }

        if (subscriptionsDto.id() == null) {
            subscriptionsService.addSubscriptions(subscriptionsDto.toEntity());
            redirectAttributes.addFlashAttribute("successMessage", "Subscription added successfully!");
        }
        else {
            Subscriptions subscriptions = subscriptionsService.findByIdSubscriptions(subscriptionsDto.id());

            if(subscriptions != null){
                subscriptions.setName(subscriptionsDto.name());
                subscriptions.setAmount(subscriptionsDto.amount());
                subscriptions.setDays(subscriptionsDto.days());
                subscriptions.setDescription(subscriptionsDto.description());
                subscriptionsService.updateSubscriptions(subscriptions);
                redirectAttributes.addFlashAttribute("successMessage", "Subscription updated successfully!");
            }
            else{
                redirectAttributes.addFlashAttribute("errorMessage", "Subscription not found for update.");
            }
        }

        return "redirect:/subscriptions";
    }

    @PostMapping("/subscriptions/delete")
    public String deleteSubscription(@RequestParam("id") Integer id,RedirectAttributes redirectAttributes) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username);

        if (user == null) {
            return "redirect:/";
        }

        subscriptionsService.deleteSubscriptions(id);
        redirectAttributes.addFlashAttribute("successMessage", "Subscription deleted successfully!");

        return "redirect:/subscriptions";
    }
}
