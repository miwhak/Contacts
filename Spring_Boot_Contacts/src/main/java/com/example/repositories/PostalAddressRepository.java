package com.example.repositories;

import com.example.model.PostalAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostalAddressRepository extends JpaRepository<PostalAddress, Long> {
    Optional<PostalAddress> findByStreetAndCityAndStateAndPostalCodeAndCountry(
            String street,
            String city,
            String state,
            String postalCode,
            String country
    );
}
