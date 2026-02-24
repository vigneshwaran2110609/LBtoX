package com.example.LBtoX.services;

import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CycleService {
	
	private final EntityManager entityManager;
	
	public CycleService(EntityManager entityManger) {
		this.entityManager = entityManger;
	}
	
	@Transactional
    public void startNewCycle(Long cycleId) {
		entityManager.createNativeQuery("""
	            INSERT INTO profile_processing (profile_id, cycle_id, status)
	            SELECT id, :cycleId, 'PENDING'
	            FROM letterboxd_profile
	        """)
	        .setParameter("cycleId", cycleId)
	        .executeUpdate();
	}
}
	