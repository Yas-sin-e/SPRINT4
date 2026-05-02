package com.yassine.employee.restcontrollers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yassine.employee.Repository.GradeRepository;
import com.yassine.employee.entity.Grade;
import com.yassine.employee.service.GradeService;

import jakarta.transaction.Transactional;

import org.springframework.web.bind.annotation.RequestMethod; 

@RestController
@RequestMapping("/api/Grade")

public class GradeRESTController {

    @Autowired
    private GradeService gradeService;

    @PostMapping
    public Grade createGrade(@RequestBody Grade grade) {
        return gradeService.saveGrade(grade);
    }

    @PutMapping("/{id}")
    public Grade updateGrade(@PathVariable Integer id, @RequestBody Grade gradeDetails) {
        return gradeService.updateGrade(id, gradeDetails);
    }

    @GetMapping
    public List<Grade> getAllGrades() {
        return gradeService.getAllGrades();
    }

    @GetMapping("/{id}")
    public Grade getGradeById(@PathVariable Integer id) {
        return gradeService.getGradeById(id);
    }
    @Transactional
    @DeleteMapping("/{id}")
    public void deleteGrade(@PathVariable Integer id) {
        gradeService.deleteGrade(id);
    }
}
