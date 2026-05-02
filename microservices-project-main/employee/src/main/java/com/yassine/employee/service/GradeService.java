package com.yassine.employee.service;

import java.util.List;

import com.yassine.employee.entity.Grade;

public interface GradeService {
	Grade saveGrade(Grade grade);            // Ajouter ou mettre à jour
    Grade updateGrade(Integer id, Grade grade); // Mettre à jour existant
    List<Grade> getAllGrades();
    Grade getGradeById(Integer id);
    void deleteGrade(Integer id);
}
