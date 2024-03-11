package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Grade;
import com.example.demo.repository.GradeRepository;
import com.example.demo.repository.GradeRepository.ScoreProjection;

import java.util.List;

@Service
public class GradeService {

    @Autowired
    private GradeRepository gradeRepository;

    // 使用名稱查詢
    public List<ScoreProjection> findByName(String name) {
        return gradeRepository.findByName(name);
    }

    // 使用分數查詢
    public List<Grade> findByScore(int score) {
        return gradeRepository.findByScore(score);
    }

    // 使用班級名稱查詢
    public List<Grade> findByClassName(String className) {
        return gradeRepository.findByClassname(className);
    }

    // 使用分數範圍查詢
    public List<Grade> findByScoreBetween(int minScore, int maxScore) {
        return gradeRepository.findByScoreBetween(minScore, maxScore);
    }

    // 使用自訂 JPQL 查詢
    public List<Grade> findWithScoreGreaterThan(int minScore) {
        return gradeRepository.findWithScoreGreaterThan(minScore);
    }
    
    public List<Grade> getGradesByTeacherClassname(String classname) {
    	System.out.println("Service : " + classname);
        return gradeRepository.findByTeacher_Classname(classname);
    }

}