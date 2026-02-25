package com.example.LBtoX.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import com.example.LBtoX.models.*;
import java.util.*;

@Repository
public interface ProfileProcessingRepository extends JpaRepository<ProfileProcessing, ProfileProcessingId>{
	@Query(value = """
			SELECT profile_id
	        FROM profile_processing
	        WHERE cycle_id = :cycleId
	        AND status = 'PENDING'
	        ORDER BY profile_id
	        LIMIT :batchSize
	        FOR UPDATE SKIP LOCKED
	        """, nativeQuery = true)
	    	List<Long> lockNextBatch(
	            @Param("cycleId") Long cycleId,
	            @Param("batchSize") int batchSize
	    );

	    @Modifying
	    @Query(value = """
	        UPDATE profile_processing
	        SET status = 'DONE'
	        WHERE cycle_id = :cycleId
	        AND profile_id IN (:ids)
	        """, nativeQuery = true)
	    void markDone(
	            @Param("cycleId") Long cycleId,
	            @Param("ids") List<Long> ids
	    );
	 
	    @Modifying
	    @Query(value = """
	        DELETE FROM profile_processing
	        WHERE cycle_id = :cycleId
	        """, nativeQuery = true)
	    void deleteByCycleId(@Param("cycleId") Long cycleId);
	}	
	
