package com.example;

import com.example.Service.ContactService;
import com.example.model.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class GreetingController {

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    @GetMapping({"/", "/home"})
    public String home() {
        return "home";
    }

    @GetMapping("/contact")
    public String showContactForm(Model model) {
        model.addAttribute("contact", new Contact());
        return "contact";
    }
    @Autowired
    private ContactService contactService;

    @PostMapping("/contact")
    public String saveContact(@ModelAttribute Contact contact, Model model) {
        contactService.saveContact(contact);
        return "redirect:/home";
    }
    public String contact(Model model) {
        model.addAttribute("contact", new Contact());
        return "contact";
    }

    @GetMapping("/contact/email")
    public String email() {
        return "email";
    }



    @PostMapping("/contact/email")
    public String addEmail(@RequestParam String contactId, @RequestParam String email, Model model) {
        Long id;
        try {
            id = Long.parseLong(contactId);
        } catch (NumberFormatException e) {
            model.addAttribute("errorMessage", "Invalid Contact ID format");
            return "error";
        }

        Contact contact = contactService.getContact(id);
        if (contact == null) {
            model.addAttribute("errorMessage", "Contact not found");
            return "error";
        }

        String message = contactService.checkAndAddEmailToContact(contact, email);
        model.addAttribute("message", message);
        return "redirect:/home";
    }


    @GetMapping("/contact/address")
    public String address() {
        return "address";
    }
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        System.out.println(e.getMessage());
        model.addAttribute("errorMessage", e.getMessage());
        return "error";
    }

}
