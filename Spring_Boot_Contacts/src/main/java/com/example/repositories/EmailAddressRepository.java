package com.example.repositories;

import com.example.model.EmailAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailAddressRepository extends JpaRepository<EmailAddress, Long> {
    Optional<EmailAddress> findByAddress(String address);
    }


