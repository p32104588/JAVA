package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Grade;
import com.example.demo.repository.GradeRepository;
import com.example.demo.repository.GradeRepository.ScoreProjection;
import com.example.demo.service.GradeService;

@RestController
@RequestMapping("/api/grades")
@RepositoryRestResource(collectionResourceRel = "grades", path = "grades")
public class GetController {

	@Autowired
    private GradeService gradeService;
    private final GradeRepository gradeRepository;

    public GetController(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }
    
    @GetMapping("/gradesbyname")
    public List<ScoreProjection> getGradesByName(@RequestParam(name = "name") String name) {
        return gradeService.findByName(name);
    }

    @GetMapping("/{id}")
    public Grade getGradeById(@PathVariable(name = "id") Long id) {
        return gradeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grade not found with id " + id));
    }
    
    
    @GetMapping("/search/findByTeacher_Classname/{classname}")
    public ResponseEntity<List<Grade>> getGradesByTeacherClassname(@PathVariable(name = "classname") String classname) {
    	System.out.println("Controller : " + classname);
        List<Grade> grades = gradeService.getGradesByTeacherClassname(classname);
        return new ResponseEntity<>(grades, HttpStatus.OK);
    }
    
}
