package com.example.LBtoX.services;

import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.example.LBtoX.repositories.*;


@Service
public class CycleService {
	
	private final EntityManager entityManager;
	
	private final ProfileProcessingRepository processingRepository;
	
	public CycleService(EntityManager entityManger, ProfileProcessingRepository processingRepository) {
		this.entityManager = entityManger;
		this.processingRepository = processingRepository;
	}
	
	@Transactional
    public void startNewCycle(Long cycleId) {
		entityManager.createNativeQuery("""
	            INSERT INTO profile_processing (profile_id, cycle_id, status)
	            SELECT id, :cycleId, 'PENDING'
	            FROM letterboxd_profiles
	        """)
	        .setParameter("cycleId", cycleId)
	        .executeUpdate();
	}

	@Transactional
	public void cleanupCycle(Long cycleId) {
	    this.processingRepository.deleteByCycleId(cycleId);
	}
}
	