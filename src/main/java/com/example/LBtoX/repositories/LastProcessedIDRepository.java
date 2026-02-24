package com.example.LBtoX.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.LBtoX.models.*;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface LastProcessedIDRepository extends JpaRepository<ProcessingCheckpoint, Long>{
	
	@Query("SELECT p FROM ProcessingCheckpoint p")
	ProcessingCheckpoint getCheckpoint();

}