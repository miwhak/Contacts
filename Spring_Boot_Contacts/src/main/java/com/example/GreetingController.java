package com.example;

import com.example.Service.ContactService;
import com.example.model.Contact;
import com.example.model.PostalAddress;
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
    /*public String contact(Model model) {
        model.addAttribute("contact", new Contact());
        return "contact";
    }*/

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

    @PostMapping("/contact/address")
    public String addAddress(@RequestParam String contactId,
                             @RequestParam String street,
                             @RequestParam String city,
                             @RequestParam String state,
                             @RequestParam String postalCode,
                             @RequestParam String country,
                             Model model) {
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

        PostalAddress address = new PostalAddress();
        address.setStreet(street);
        address.setCity(city);
        address.setState(state);
        address.setPostalCode(postalCode);
        address.setCountry(country);

        String message = contactService.checkAndAddAddressToContact(contact, address);
        model.addAttribute("message", message);
        return "redirect:/home";
    }




    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        System.out.println(e.getMessage());
        model.addAttribute("errorMessage", e.getMessage());
        return "error";
    }

}
