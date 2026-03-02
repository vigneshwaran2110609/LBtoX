package com.example.LBtoX.repositories;

import com.example.LBtoX.models.TwitterCredential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TwitterCredentialRepository
        extends JpaRepository<TwitterCredential, Long> {

    Optional<TwitterCredential> findByLetterboxdId(String letterboxdId);

    Optional<TwitterCredential> findByLetterboxdIdAndIsActiveTrue(String letterboxdId);

}