package com.example.LBtoX.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import com.example.LBtoX.models.*;

@Repository
public interface ProcessingCheckpointRepository extends JpaRepository<ProcessingCheckpoint, Long>{
	
	@Query("SELECT p FROM ProcessingCheckpoint p")
    ProcessingCheckpoint findCheckpoint();

}
