package com.example.LBtoX.repositories;

import com.example.LBtoX.models.LetterboxdProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RssFeedRepository extends JpaRepository<LetterboxdProfile, Long>{
    List<LetterboxdProfile> findByIdBetween(Long startId, Long endId);
}
