package com.example.LBtoX.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.LBtoX.models.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface LastProcessedIDRepository extends JpaRepository<ProcessingCheckpoint, Long>{
	
	@Query("SELECT p FROM ProcessingCheckpoint p")
	ProcessingCheckpoint getCheckpoint();
	
	@Modifying
	@Transactional
	@Query(value = """
	       UPDATE processing_checkpoint
	       SET last_processed_id = :lastProcessedId
	       """, nativeQuery = true)
	int updateLastProcessedId(
	        @Param("lastProcessedId") Long lastProcessedId
	);

}

