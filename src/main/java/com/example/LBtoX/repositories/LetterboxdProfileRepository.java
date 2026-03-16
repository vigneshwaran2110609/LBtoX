package com.example.LBtoX.repositories;

import com.example.LBtoX.models.LetterboxdProfile;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository
public interface LetterboxdProfileRepository extends JpaRepository<LetterboxdProfile, Long> {

    Optional<LetterboxdProfile> findByLetterboxdId(String letterboxdId);

    boolean existsByLetterboxdId(String letterboxdId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM LetterboxdProfile p WHERE p.id = :id")
    LetterboxdProfile findByIdForUpdate(@Param("id") Long id);
}