package com.ktx.module.room;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoomRepository extends JpaRepository<Room, String> {
	
	@EntityGraph(attributePaths = {"students"})
	@Query("SELECT r FROM Room r WHERE (r.name LIKE %?1%) OR (r.code LIKE %?1%)")
	List<Room> findAllAndFetchStudents(String keyword);
}