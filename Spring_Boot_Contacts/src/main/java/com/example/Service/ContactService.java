package com.example.Service;

import com.example.model.Contact;
import com.example.model.EmailAddress;
import com.example.model.PostalAddress;
import com.example.repositories.ContactRepository;
import com.example.repositories.EmailAddressRepository;
import com.example.repositories.PostalAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private EmailAddressRepository emailAddressRepository;
    @Autowired
    private PostalAddressRepository postalAddressRepository;

    public Contact saveContact(Contact contact) {
        return contactRepository.save(contact);
    }

    public String checkAndAddEmailToContact(Contact contact, String emailAddress) {
        Optional<EmailAddress> optionalEmailAddress = emailAddressRepository.findByAddress(emailAddress);
        if (optionalEmailAddress.isPresent()) {
            EmailAddress existingEmailAddress = optionalEmailAddress.get();
            if (existingEmailAddress.getContact().getId().equals(contact.getId())) {
                throw new IllegalArgumentException("Email address already exists for this contact.");
            } else {
                throw new IllegalArgumentException("Email address is already used for another contact.");
            }
        } else {
            EmailAddress newEmailAddress = new EmailAddress();
            newEmailAddress.setAddress(emailAddress);
            newEmailAddress.setContact(contact);
            contact.getEmailAddresses().add(newEmailAddress);
            contactRepository.save(contact);
        }
        return "Email address added successfully to the contact";

    }
    public String checkAndAddAddressToContact(Contact contact, PostalAddress address) {
        Optional<PostalAddress> existingAddress = postalAddressRepository.findByStreetAndCityAndStateAndPostalCodeAndCountry(
                address.getStreet(),
                address.getCity(),
                address.getState(),
                address.getPostalCode(),
                address.getCountry()
        );

        if (existingAddress.isPresent()) {
            if (contact.getPostalAddresses().contains(existingAddress.get())) {
                return "The contact already has this postal address";
            }
            existingAddress.get().addContact(contact);
            postalAddressRepository.save(existingAddress.get());
            return "Postal address added to the contact";
        } else {
            address.addContact(contact);
            postalAddressRepository.save(address);
            return "New postal address created and added to the contact";
        }
    }

    public Contact getContact(Long id) {
        return contactRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid contact Id:" + id));
    }


    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

}
