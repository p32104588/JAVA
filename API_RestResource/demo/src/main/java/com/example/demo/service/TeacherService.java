package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Teacher;
import com.example.demo.repository.GradeRepository;
import com.example.demo.repository.TeacherRepository;
import com.example.demo.repository.GradeRepository.ScoreProjection;

@Service
public class TeacherService {
	@Autowired
    private TeacherRepository teacherRepository;
	
    public List<Teacher> findByTeacherName(String name) {
        return teacherRepository.findByTeacher(name);
    }
}
