package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.demo.model.Grade;


@RepositoryRestResource(path = "grade")
public interface GradeRepository extends JpaRepository<Grade, Long> {
	// 透過名稱查詢
    List<ScoreProjection> findByName(String name);
    
    // 透過分數查詢
    List<Grade> findByScore(@Param("score")int score);
    
    // 透過班級名稱查詢
    List<Grade> findByClassname(@Param("classname")String classname);
    
    // 查詢分數在指定範圍內的記錄
    List<Grade> findByScoreBetween(@Param("minScore")int minScore, @Param("maxScore")int maxScore);
    
    // 自訂查詢，使用 JPQL
    @Query("SELECT g FROM Grade g WHERE g.score > :minScore")
    List<Grade> findWithScoreGreaterThan(@Param("minScore") int minScore);
    
    
    @Query("SELECT g FROM Grade g JOIN g.teacher WHERE g.classname = :classname")
    List<Grade> findByTeacher_Classname(@Param("classname")String classname);
    
    
    public interface ScoreProjection {
        int getScore();
    }
    
}