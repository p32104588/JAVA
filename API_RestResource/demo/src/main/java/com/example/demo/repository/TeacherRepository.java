package com.example.demo.repository;

import java.util.List;
import com.example.demo.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "teacher")
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    List<Teacher> findByTeacher(@Param("teacher")String teacher);
}