package com.ktx.module.student;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, String> {

    @EntityGraph(attributePaths = "room")
    @Query("SELECT s FROM Student s WHERE s.name LIKE %?1% OR s.studentId LIKE %?1%")
    List<Student> findAllAndFetchRoom(String keyword);
}
