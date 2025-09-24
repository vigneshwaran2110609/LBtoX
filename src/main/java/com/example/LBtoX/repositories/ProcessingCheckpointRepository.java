package com.example.LBtoX.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import com.example.LBtoX.models.*;

@Repository
public interface ProcessingCheckpointRepository extends JpaRepository<ProcessingCheckpoint, Long>{
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT p FROM ProcessingCheckpoint p")
    ProcessingCheckpoint findCheckpoint();
	
	@Modifying
	@Query("UPDATE ProcessingCheckpoint p SET p.lastProcessedId = :newValue where p.id = 1")
	int updateLastProcessedId(@Param("newValue") Long newValue);
	
	

}
