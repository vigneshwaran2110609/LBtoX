package com.example.LBtoX.repositories;

import com.example.LBtoX.models.LetterboxdProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository
public interface LetterboxdProfileRepository extends JpaRepository<LetterboxdProfile, Long> {

    Optional<LetterboxdProfile> findByLetterboxdId(String letterboxdId);

    boolean existsByLetterboxdId(String letterboxdId);
}