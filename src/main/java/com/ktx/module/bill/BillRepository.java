package com.ktx.module.bill;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BillRepository extends JpaRepository<Bill, String> {

	@EntityGraph(attributePaths = "room")
	@Query("SELECT b FROM Bill b WHERE b.code LIKE %?1%")
	List<Bill> findAllAndFetch(String keyword);
}