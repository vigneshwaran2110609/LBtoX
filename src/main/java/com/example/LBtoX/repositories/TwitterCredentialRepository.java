package com.example.LBtoX.repositories;

import com.example.LBtoX.models.TwitterCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TwitterCredentialRepository extends JpaRepository<TwitterCredential, Long> {
    Optional<TwitterCredential> findByLetterboxdId(String letterboxdId);
    Optional<TwitterCredential> findByTwitterId(String twitterId);
    Optional<TwitterCredential> findByOauthState(String oauthState);
}
