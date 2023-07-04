package com.ktx.module.electronic;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ElectronicRepository extends JpaRepository<Electronic, String> {
	
	@EntityGraph(attributePaths = "room")
	@Query("SELECT e FROM Electronic e WHERE e.code LIKE %?1%")
	List<Electronic> findAllAndFetch(String keyword);
}