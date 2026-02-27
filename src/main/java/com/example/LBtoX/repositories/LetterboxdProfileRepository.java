package com.example.LBtoX.repositories;

import com.example.LBtoX.models.LetterboxdProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository
public interface LetterboxdProfileRepository extends JpaRepository<LetterboxdProfile, Long> {

    // Find by Letterboxd username
    Optional<LetterboxdProfile> findByLetterboxdId(String letterboxdId);

    // Check existence to avoid duplicates
    boolean existsByLetterboxdId(String letterboxdId);
}